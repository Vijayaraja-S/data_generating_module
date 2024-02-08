package com.p3.poc.bean;

import com.p3.export.formatter.ExportFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DataGeneratorBean {
    private String databaseName;
    private String schemaName;
    private List<TableEntity>tableInfos;
    private String outputPath;
    private ExportFormat outputFormat;
}
