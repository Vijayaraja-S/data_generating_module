package com.p3.export.utility.parquet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.api.WriteSupport;
import org.apache.parquet.schema.MessageType;

import java.util.List;

public class ParquetWriterBuilder
    extends ParquetWriter.Builder<List<Object>, ParquetWriterBuilder> {
  private MessageType type = null;

  public ParquetWriterBuilder(Path file) {
    super(file);
  }

  public ParquetWriterBuilder withType(MessageType type) {
    this.type = type;
    return this;
  }

  @Override
  protected ParquetWriterBuilder self() {
    return this;
  }

  @Override
  protected WriteSupport<List<Object>> getWriteSupport(Configuration conf) {
    return new ParquetWriteSupport(type);
  }
  public ParquetWriterBuilder withDictionaryEncodingSetup(){
    this.withDictionaryEncoding(false);
    this.withValidation(false);
    this.withPageSize(3000000);
    this.withRowGroupSize(30000000); //30 MB
    return this;
  }
  // 128 MB - 30 MB - 3 MB
  // 1 GB - 128 MB - 30 MB
}
