package com.p3.export.operation;

import com.p3.export.file_utils.P3FileCommonUtils;
import com.p3.export.formatter.ExportFormat;
import com.p3.export.logutils.P3LoggerUtils;
import com.p3.export.options.ColumnInfo;
import com.p3.export.options.Options;
import lombok.Data;
import org.zeroturnaround.zip.ZipUtil;
import org.zeroturnaround.zip.commons.FileUtils;


import java.io.File;
import java.util.List;

@Data
public final class ExportEngineExecutable {

  private final Options options;
  private final String title;
  private final List<ColumnInfo> columns;
  private ExportEngineHandler createdHandler;
  private final P3LoggerUtils logger;

  public ExportEngineExecutable(Options options, String title, List<ColumnInfo> columnsInfo) {
    this.options = options;
    this.title = title;
    this.columns = columnsInfo;
    this.logger = new P3LoggerUtils(ExportEngineExecutable.class);
  }

  public ExportEngineHandler getExportEngineHandler() throws Exception {
    final ExportEngineHandler handler = getExecutionHandler();
    createdHandler = handler;
    return handler;
  }

  private ExportEngineHandler getExecutionHandler() throws Exception {
    switch (options.getExportFormat()) {
      case dynamic_export_html:
      case dynamic_export_html_blob_only:
      case js:
        return new JsExportEngine(this.options, title, columns);
      case json:
        return new JsonExportEngine(this.options, title, columns);
      case excel:
        return new ExcelExportEngine(this.options, title, columns);
      case xml:
        return new XmlExportEngine(this.options, title, columns);
      case xslt_html:
        return new XsltHtmlExportEngine(this.options, title, columns);
      case xsl_pdf:
        return new XslPdfExportEngine(this.options, title, columns);
      case parquet:
        return new ParquetExportEngine(this.options, title, columns);
      default:
        return new TextExportEngine(this.options, title, columns);
    }
  }

  public void generateReport() {
    logger.info("Total Records Processed : {} : {} ", title, options.getRecordsProcessed());
  }

  public String zipOutput() {
    final File sourcePath = new File(options.getOutputFolderPath());
    File output =
        P3FileCommonUtils.createFile(
            sourcePath.getParentFile() + File.separator + options.getOutputFileTitle() + ".zip");
    ZipUtil.pack(sourcePath, output);
    FileUtils.deleteQuietly(sourcePath);
    return output.getAbsolutePath();
  }

  public String baseResultPath() {
    if (options.getExportFormat() == ExportFormat.js
        || options.getExportFormat() == ExportFormat.dynamic_export_html
        || options.getExportFormat() == ExportFormat.dynamic_export_html_blob_only) {
      return options.getOutputFolderPath();
    }
    return options.getOutputFileTitle() + options.getExportFormat().getExtension();
  }
}
