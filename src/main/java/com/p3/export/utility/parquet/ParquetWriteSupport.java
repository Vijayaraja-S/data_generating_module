package com.p3.export.utility.parquet;

import com.p3.export.specifics.BlobKeySplitBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.parquet.column.ColumnDescriptor;
import org.apache.parquet.hadoop.api.WriteSupport;
import org.apache.parquet.io.ParquetEncodingException;
import org.apache.parquet.io.api.Binary;
import org.apache.parquet.io.api.RecordConsumer;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.OriginalType;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.p3.export.utility.others.Utility.isBlank;

@Slf4j
public class ParquetWriteSupport extends WriteSupport<List<Object>> {
  MessageType schema;
  RecordConsumer recordConsumer;
  List<ColumnDescriptor> columnDescriptors;

  public ParquetWriteSupport(MessageType schema) {
    this.schema = schema;
    this.columnDescriptors = schema.getColumns();
  }

  @Override
  public WriteContext init(Configuration configuration) {
    return new WriteContext(schema, new HashMap<>());
  }

  @Override
  public void prepareForWrite(RecordConsumer recordConsumer) {
    this.recordConsumer = recordConsumer;
  }

  @Override
  public void write(List<Object> record) {
    recordConsumer.startMessage();
    for (int i = 0; i < columnDescriptors.size(); i++) {
      try {
        if (record.get(i) == null) continue;
        Object value = record.get(i);
        recordConsumer.startField(columnDescriptors.get(i).getPath()[0], i);
        switch (columnDescriptors.get(i).getPrimitiveType().getPrimitiveTypeName()) {
          case BOOLEAN:
            recordConsumer.addBoolean(Boolean.parseBoolean(value.toString()));
            break;
          case FLOAT:
            recordConsumer.addFloat(Float.parseFloat(value.toString()));
            break;
          case DOUBLE:
            recordConsumer.addDouble(Double.parseDouble(value.toString()));
            break;
          case INT32:
            if (columnDescriptors.get(i).getPrimitiveType().getOriginalType() != null
                && columnDescriptors
                    .get(i)
                    .getPrimitiveType()
                    .getOriginalType()
                    .equals(OriginalType.DATE))
              value =
                  value.toString().contains(" ")
                      ? String.valueOf(
                          LocalDate.parse(
                                  value.toString().substring(0, value.toString().indexOf(" ")))
                              .toEpochDay())
                      : String.valueOf(LocalDate.parse(value.toString()).toEpochDay());
            recordConsumer.addInteger(Integer.parseInt(value.toString()));
            break;
          case INT64:
            if (columnDescriptors.get(i).getPrimitiveType().getOriginalType() != null
                && columnDescriptors
                    .get(i)
                    .getPrimitiveType()
                    .getOriginalType()
                    .equals(OriginalType.TIMESTAMP_MILLIS)) {
              value = getTimestampObjectValue(value);
            }
            recordConsumer.addLong(Long.parseLong(value.toString()));
            break;
          case INT96:
            recordConsumer.addLong(Long.parseLong(value.toString()));
            break;
          case BINARY:
          case FIXED_LEN_BYTE_ARRAY:
            if (value instanceof BlobKeySplitBean) {
              final BlobKeySplitBean blobKeySplitbean = (BlobKeySplitBean) value;
              if (isBlank(blobKeySplitbean.getError())) {
                recordConsumer.addBinary(null);
              } else {
                String data =
                    blobKeySplitbean
                            .getRelativePath()
                            .substring(blobKeySplitbean.getRelativePath().lastIndexOf("/") + 1)
                        + "::"
                        + blobKeySplitbean.getOutputFileName();
                recordConsumer.addBinary(Binary.fromString(data));
              }
            } else {
              recordConsumer.addBinary(Binary.fromString(value.toString()));
            }
            break;
          default:
            throw new ParquetEncodingException();
        }
        recordConsumer.endField(columnDescriptors.get(i).getPath()[0], i);
      } catch (Exception e) {
        log.error(
            "Error at column : " + columnDescriptors.get(i) + " . column value : " + record.get(i),
            e);
        throw e;
      }
    }
    recordConsumer.endMessage();
  }

  /**
   * Timestamp value
   *
   * @param value
   * @return
   */
  private static Object getTimestampObjectValue(Object value) {
    try {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
      Date parsedDate = dateFormat.parse(value.toString().replace("T", " "));
      Timestamp timestamp = new Timestamp(parsedDate.getTime());
      value = String.valueOf(timestamp.getTime());
    } catch (Exception e) {
      value = "0l";
    }
    return value;
  }
}
