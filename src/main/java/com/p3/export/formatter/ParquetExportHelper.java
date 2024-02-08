package com.p3.export.formatter;

import com.p3.export.options.ColumnInfo;
import com.p3.export.specifics.DataType;
import com.p3.export.specifics.ExcelSpecificDataType;
import com.p3.export.utility.others.FileUtil;
import com.p3.export.utility.parquet.ParquetWriterBuilder;

import com.p3.poc.bean.ColumnEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.apache.parquet.schema.*;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.p3.export.utility.CommonMessageConstants.ERROR_LOG_TEMPLATE;

@Slf4j
public class ParquetExportHelper implements TextExportHelper {

  MessageType parquetSchema;
  ParquetWriter<List<Object>> parquetWriter;
  Configuration hadoopConfig;
  String title;
  String outputFolderPath;
  PrintWriter attachmentFileWriter;
  String attachmentFileFolderName;
  String parentFolder;

  public ParquetExportHelper(String title, List<ColumnInfo> columnsInfo, String outputPath, String outputTitle,
                             String outputFolderPath) {
    try {
      parquetSchema = createParquetSchema(title.substring(title.indexOf("-") + 1), columnsInfo);
      hadoopConfig =
          new Configuration() {
            public Class<?> getClassByName(String name) throws ClassNotFoundException {
              return Class.forName(name);
            }
          };
      hadoopConfig.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
      parquetWriter = createParquetWriter(outputTitle,outputPath);
      this.outputFolderPath = outputFolderPath;
      this.title = title;
    } catch (Exception exception) {
      log.error(ERROR_LOG_TEMPLATE, exception);
    }
  }

  private MessageType createParquetSchema(String title, List<ColumnInfo> columnInfoList) {
    MessageType schema;
    Types.MessageTypeBuilder schemaBuilder = Types.buildMessage();
    for (ColumnInfo columnInfo : columnInfoList) {
      switch (columnInfo.getDataType()) {
        case BOOLEAN:
          schemaBuilder.addField(
              new PrimitiveType(
                  Type.Repetition.OPTIONAL,
                  PrimitiveType.PrimitiveTypeName.BOOLEAN,
                  columnInfo.getColumn()));
          break;
        case DECIMAL:
          schemaBuilder.addField(
              new PrimitiveType(
                  Type.Repetition.OPTIONAL,
                  PrimitiveType.PrimitiveTypeName.DOUBLE,
                  columnInfo.getColumn()));
          break;
        case NUMBER:
          schemaBuilder.addField(
              new PrimitiveType(
                  Type.Repetition.OPTIONAL,
                  PrimitiveType.PrimitiveTypeName.INT64,
                  columnInfo.getColumn()));
          break;
        case DATE:
          schemaBuilder.addField(
              new PrimitiveType(
                  Type.Repetition.OPTIONAL,
                  PrimitiveType.PrimitiveTypeName.INT32,
                  columnInfo.getColumn(),
                  OriginalType.DATE));
          break;
        case DATETIME:
          schemaBuilder.addField(
              new PrimitiveType(
                  Type.Repetition.OPTIONAL,
                  PrimitiveType.PrimitiveTypeName.INT64,
                  columnInfo.getColumn(),
                  OriginalType.TIMESTAMP_MILLIS));
          break;
        case BLOB:
        case STRING:
        default:
          schemaBuilder.addField(
              new PrimitiveType(
                  Type.Repetition.OPTIONAL,
                  PrimitiveType.PrimitiveTypeName.BINARY,
                  columnInfo.getColumn(),
                  OriginalType.UTF8));
          break;
      }
    }
    schema = schemaBuilder.named(title);
    return schema;
  }

  @Override
  public TextExportHelper append(String text) {
    return this;
  }

  @Override
  public void writeDocumentEnd() throws Exception {
    if (parquetWriter != null) {
      parquetWriter.close();
    }
    if (attachmentFileWriter != null) {
      attachmentFileWriter.flush();
      attachmentFileWriter.close();
    }
    disposeHandler();
  }

  private void disposeHandler() {
    File tableFolderDirectory = new File(this.parentFolder);
    File[] tableFolderDirectoryFiles = tableFolderDirectory.listFiles((d, s) -> s.toLowerCase().endsWith(".txt"));
    try {
      if (tableFolderDirectoryFiles != null && tableFolderDirectoryFiles.length > 0) {
        for (File attachmentFile : tableFolderDirectoryFiles) {
          String blobTableFolderPath = tableFolderDirectory + File.separator + "blobs" + File.separator;
          String individualBlobFolderPath = blobTableFolderPath + File.separator + attachmentFile.getName().replace(".txt", "");
          FileUtil.createDir(individualBlobFolderPath);
          try (BufferedReader bufferedReader = new BufferedReader(new FileReader(attachmentFile))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
              if (!StringUtils.isEmpty(line)) {
                File sourceFile = FileUtil.createFile(blobTableFolderPath + File.separator + line);
                File targetFile = FileUtil.createFile(individualBlobFolderPath + File.separator + line);
                Files.move(Paths.get(sourceFile.getPath()), Paths.get(targetFile.getPath()), StandardCopyOption.REPLACE_EXISTING);
              }
            }
            Files.delete(Path.of(attachmentFile.getAbsolutePath()));
          }
          File attachmentFolder = FileUtil.createFile(individualBlobFolderPath);
          if (attachmentFolder.listFiles().length == 0) {
            attachmentFolder.delete();
            log.info("Successfully deleted individual blobfolder :");
          }
        }
        File attachmentBlob = FileUtil.createFile(tableFolderDirectory+File.separator+"blobs");
        if (attachmentBlob.listFiles().length == 0) {
          attachmentBlob.delete();
          log.info("Successfully deleted blobfolder :");
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void writeDocumentStart() throws Exception {}

  @Override
  public void writeEmptyRow() {}

  @Override
  public void writeRow(List<Object> columnData, List<ExcelSpecificDataType> excelSpecificDataTypes,
                       List<DataType> dataTypes) throws Exception {

  }

  @Override
  public void writeRow(List<Object> currentRow, List<ExcelSpecificDataType> excelSpecificDataTypes,
                       List<ColumnEntity> columnEntities, List<String> attachementList) throws Exception {
    if (attachmentFileWriter != null) {
      for (String attachmentFileName : attachementList) {
        attachmentFileWriter.write(attachmentFileName);
        attachmentFileWriter.write("\n");
        attachmentFileWriter.flush();
      }
    }
    parquetWriter.write(processRow(currentRow,columnEntities));
    Long size = parquetWriter.getDataSize();
    if(size >= 134217728 ){
      log.info("File Size : {}",size);
      parquetWriter.close();
      String outputFileTitle = title + "-" + UUID.randomUUID();
      String outputFile = outputFolderPath + File.separator + outputFileTitle + ".parquet";
      parquetWriter = createParquetWriter(outputFileTitle,outputFile);
    }
  }

  private List<Object> processRow(List<Object> currentRow, List<ColumnEntity> columnEntities) {
    List<Object> objectList = new ArrayList<>();
    for (int i = 0; i < currentRow.size(); i++) {
      if (columnEntities.get(i).getType().equals(DataType.BLOB.toString())) {
        String objectValue = currentRow.get(i).toString();
        objectValue = attachmentFileFolderName + "::" + objectValue;
        objectList.add(objectValue);
      }else{
        objectList.add(currentRow.get(i));
      }

    }
    return objectList;
  }

  private ParquetWriter<List<Object>> createParquetWriter(String outputFileTitle,String outputPath) throws IOException {
    this.parentFolder = new File(outputPath).getParentFile().getAbsolutePath();
    this.attachmentFileFolderName = outputFileTitle;
    this.attachmentFileWriter = new PrintWriter(parentFolder +File.separator+ outputFileTitle + ".txt");
    return new ParquetWriterBuilder(new org.apache.hadoop.fs.Path(outputPath))
                    .withConf(hadoopConfig)
                    .withType(parquetSchema)
                    .withCompressionCodec(CompressionCodecName.UNCOMPRESSED)
                    .withDictionaryEncodingSetup()
                    .build();
  }

  @Override
  public void writeRowHeader(List<String> columnNames) throws Exception {}

  @Override
  public void flush() {}

  @Override
  public void close() throws Exception {}




}
