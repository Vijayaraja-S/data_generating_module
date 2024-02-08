package com.p3.export.formatter;

import com.p3.export.exceptions.ExportException;
import com.p3.export.logutils.P3LoggerUtils;
import com.p3.export.options.ColumnInfo;
import com.p3.export.options.Options;
import org.apache.parquet.hadoop.ParquetWriter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static java.util.Objects.requireNonNull;

public abstract class BaseExporter {

  private static final long RECORD_PROCESS_LOG_CHECKER_VALUE = 100000;
  private final P3LoggerUtils logger;
  protected TextExportHelper exportHelper;
  protected Options options;
  protected PrintWriter out;
  protected ParquetWriter<List<Object>> parquetWriter;
  protected List<ColumnInfo> columnsInfo;
  protected String title;

  public BaseExporter(Options outputOptions, List<ColumnInfo> columnsInfo, String title)
      throws Exception {
    this.options = requireNonNull(outputOptions, "Options not provided");
    this.title = title;
    this.columnsInfo = columnsInfo;
    this.logger = new P3LoggerUtils(BaseExporter.class);
    if (this.options.getExportFormat().equals(ExportFormat.parquet)) {

    } else {
      try {
        out = new PrintWriter(options.openNewOutputWriter(true), true);
      } catch (final IOException e) {
        throw new ExportException("Cannot open output writer", e);
      }
    }
    setFormattingHelper();
  }

  protected void setFormattingHelper() {
    final ExportFormat exportFormat = options.getExportFormat();
    switch (exportFormat) {
      case csv:
      case tsv:
      case txt:
      case ssv:
        exportHelper = new PlainTextExportHelper(out, exportFormat);
        break;
      case dynamic_export_html:
      case dynamic_export_html_blob_only:
      case js:
        exportHelper = new JsExportHelper(out, exportFormat);
        break;
      case json:
        exportHelper = new JsonExportHelper(out, exportFormat);
        break;
      case html:
        exportHelper = new HtmlExportHelper(out, exportFormat);
        break;
      case excel:
        exportHelper = new ExcelExportHelper(options.getOutputFile().toFile(), exportFormat, title);
        break;
      case xml:
      case xslt_html:
      case xsl_pdf:
        exportHelper = new XmlExportHelper(options.isXmlCaseSensitive(), out, exportFormat);
        break;
      case parquet:
        exportHelper =
            new ParquetExportHelper(
                title, columnsInfo, options.getOutputFile().toFile().getAbsolutePath(),
                    options.getOutputFileTitle(),options.getOutputFolderPath());
        break;
    }
  }

  protected void generateProgressReport(String title) {
    if (options.getRecordsProcessed() % RECORD_PROCESS_LOG_CHECKER_VALUE == 0) {
      logger.info("Records Processed : {} : {} ", title, options.getRecordsProcessed());
    }
  }
}
