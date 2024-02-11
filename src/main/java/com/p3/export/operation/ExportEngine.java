package com.p3.export.operation;

import com.p3.export.formatter.ExportFormat;
import com.p3.export.options.ColumnInfo;
import com.p3.export.options.Options;
import lombok.Builder;
import lombok.Data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.p3.poc.bean.ColumnEntity;
@Data
@Builder
public class ExportEngine {

  private final String basePath;
  private final String title;
  private String xsltFilePath;
  private ExportFormat exportFormat;
  private List<ColumnInfo> columnsInfo;
  private ExportEngineExecutable oe;
  private ExportEngineHandler eh;

  public ExportEngineHandler getExportEngineHandler() throws Exception {
    Options options = initiateOptions();
    oe = new ExportEngineExecutable(options, title, columnsInfo);
    return oe.getExportEngineHandler();
  }

  private Options initiateOptions() throws IOException {
    if (exportFormat == ExportFormat.xslt_html) {
      if (xsltFilePath == null) {
        exportFormat = ExportFormat.dynamic_export_html;
      }
    }
    Options options = Options.builder().exportFormat(exportFormat).build();
    options.setOutputFilePath(basePath, title);
    options.setXmlCaseSensitive(xsltFilePath != null);
    options.setTemplatePath(xsltFilePath);
    return options;
  }

  private String writeXsltFile(Options options) throws IOException {
    String path = options.getOutputFolderPathPrefix() + "xslt";
    new File(path).mkdirs();
    String xsltPath = path + File.separator + "xsltFile.xslt";
    try (FileWriter fileWriter = new FileWriter(xsltPath)) {
      fileWriter.write(xsltFilePath);
      fileWriter.flush();
    }
    return xsltPath;
  }

  public void initialize() throws Exception {
    eh = getExportEngineHandler();
  }

  public void handleDataStart() throws Exception {
    eh.handleDataStart();
  }

  public void iterateRows(List<Object> values) throws Exception {
    eh.iterateRows(values);
  }

  public void handleDataEnd() throws Exception {
    eh.handleDataEnd();
  }
  public void handleDataEnd(Object reportDetails) throws Exception {
    eh.handleDataEnd(reportDetails);
  }

  public void generateReport() {
    oe.generateReport();
  }

  public String getResultPath(boolean zip) {
    return zip ? oe.zipOutput() : oe.baseResultPath();
  }

  public void iterateRows(List<Object> values, List<String> attachementList, Map<String, ColumnEntity> columnMetadata) throws Exception {
    eh.iterateRows(values,attachementList,columnMetadata.values().stream().sorted(Comparator.comparing(ColumnEntity::getOrdinal)).collect(Collectors.toList()));
  }

}
