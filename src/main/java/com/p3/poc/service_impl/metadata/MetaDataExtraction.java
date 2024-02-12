package com.p3.poc.service_impl.metadata;

import com.google.gson.Gson;
import com.p3.poc.bean.ColumnEntity;
import com.p3.poc.bean.metadata.beans.*;
import com.p3.poc.bean.metadata.enums.ColumnOrigin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MetaDataExtraction {
    @Value("${metadata.maxSize}")
    private long maxFileSize;
    public static final String JSON = ".json";
    public static final String METADATA = "metadata";
    public static Map<String, MetadataMonitoring> schemaMap = new HashMap<>();
    public static final String METADATA_GLOBAL_HEADER = "{\"metadata\": {\"schemas\": [{\"name\": \"SCHEMA_NAME\",\"sourceName\": \"SCHEMA_NAME\",\"sourceDatabaseName\": \"DATABASE_NAME\",\"tableCount\": TABLE_COUNT,\"description\": \"\",\"tables\": [";
    public static final String METADATA_FOOTER = "]}]}}";

    public  synchronized  void schemaMetadataUpdate(String schemaName, String databaseName, Integer tableCount, String outputPath) throws IOException {
        writeSchemaMetadata(schemaName, outputPath, databaseName, tableCount,1);
    }
    private  void writeSchemaMetadata(
            String schemaName,
            String outputPath, String databaseName, int tableCount, int rollNo) throws IOException {
        String schemaMetadataFile = getSchemaMetadataFile(schemaName, databaseName, outputPath,rollNo);
        File tempMetadataFile = new File(schemaMetadataFile);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempMetadataFile.getAbsolutePath(), true));
            String template = METADATA_GLOBAL_HEADER.replace("SCHEMA_NAME", schemaName).replace("DATABASE_NAME", databaseName)
                    .replace("TABLE_COUNT", String.valueOf(tableCount));
            writer.write(template);
            writer.flush();
            MetadataMonitoring writerBean = MetadataMonitoring.builder().filePath(schemaMetadataFile)
                    .writerBean(writer)
                    .template(template)
                    .header(true)
                    .tableCount(tableCount)
                    .rollNo(rollNo)
                    .build();
            schemaMap.put(schemaName, writerBean);
        } catch (IOException e) {
            log.error("cannot export Schema metadata");
            throw new IOException("cannot export schema metadata", e);
        }
    }
    public  String getSchemaMetadataFile(String schemaName, String databaseName, String outputPath,int rollNo) {
        return outputPath + File.separator + METADATA +"_"+ databaseName + "_" + schemaName + "_" +  String.format("%05d", rollNo)+ JSON;
    }
    public  synchronized void TableMetadataUpdate(String tableName,
                                                  String schemaName,
                                                  List<ColumnEntity> columnList, String databaseName, String outputPath, int recordCount) throws IOException {
        final AtomicInteger count = new AtomicInteger(1);
        List<ColumnMetadata> columnMetadataList = prepareColumnMetadata(columnList, count);
        Gson gson = new Gson();
        if (schemaMap.containsKey(schemaName)){
            MetadataMonitoring schemaWriterBean = schemaMap.get(schemaName);
            int columnCount = columnMetadataList.size();
            int tableCount = schemaWriterBean.getTableCount();
            int rollNo = schemaWriterBean.getRollNo();
            if (Files.size(Path.of(schemaWriterBean.getFilePath())) < maxFileSize) {
                writeTableMetadata(tableName, columnCount, columnMetadataList, gson, schemaName, recordCount);
            } else {
                closeFiles(schemaName);
                writeSchemaMetadata(schemaName, outputPath, databaseName, tableCount,rollNo+1);
                writeTableMetadata(tableName, columnCount, columnMetadataList, gson, schemaName, recordCount);
            }
        }
    }
    private  void writeTableMetadata(String tableName,
                                     int columnCount, List<ColumnMetadata> columnMetadataList,
                                     Gson gson, String schemaName, int recordCount) throws IOException {
        MetadataMonitoring schemaWriterBean = schemaMap.get(schemaName);
        if (!schemaWriterBean.getHeader()) {
            schemaWriterBean.getWriterBean().write(",");
        } else {
            schemaWriterBean.setHeader(false);
        }
        TableMetadata tableMetadata = TableMetadata.builder()
                .name(tableName)
                .sourceName(tableName)
                .columnCount(columnCount)
                .columns(columnMetadataList)
                .recordCount(recordCount)
                .build();
        String jsonString = gson.toJson(tableMetadata);
        schemaWriterBean.getWriterBean().write(jsonString);
        schemaWriterBean.getWriterBean().flush();
    }

    private  List<ColumnMetadata> prepareColumnMetadata(List<ColumnEntity> columnList, AtomicInteger count) {

        return columnList.stream()
                .map(columnMeta -> ColumnMetadata.builder().
                        name(columnMeta.getName())
                        .ordinal(columnMeta.getOrdinal())
                        .dataType(columnMeta.getTypeData().getDataType().toString())
                        .fieldProperties(FieldProperties.builder().build())
                        .dataProperties(DataProperties.builder().
                                additional(Additional.builder().build())
                                .build())
                        .source(Source.builder().
                                name(columnMeta.getName())
                                .type(columnMeta.getTypeData().getDataType().toString())
                                .origin(ColumnOrigin.SOURCE_SYSTEM.toString())
                                .build())
                        .build())
                .collect(Collectors.toList());
    }
    public  synchronized void closeFiles(String schemaName) throws IOException {
        schemaMap.get(schemaName).getWriterBean().write(METADATA_FOOTER);
        schemaMap.get(schemaName).getWriterBean().flush();
        schemaMap.get(schemaName).getWriterBean().close();
        schemaMap.remove(schemaName);
    }


}
