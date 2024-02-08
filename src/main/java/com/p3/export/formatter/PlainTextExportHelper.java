package com.p3.export.formatter;

import com.p3.export.specifics.ExcelSpecificDataType;
import com.p3.poc.bean.ColumnEntity;


import java.io.PrintWriter;
import java.util.List;

public class PlainTextExportHelper extends BaseTextExportHelper {

  public PlainTextExportHelper(final PrintWriter out, final ExportFormat exportFormat) {
    super(out, exportFormat);
  }

  @Override
  public void writeDocumentEnd() {}

  @Override
  public void writeDocumentStart() {}

  @Override
  public void flush() {
    out.flush();
  }

  @Override
  public void close() {
    if (out != null) {
      out.flush();
      out.close();
    }
  }

  @Override
  public void writeRow(List<Object> currentRow, List<ExcelSpecificDataType> excelSpecificDataTypes, List<ColumnEntity> columnEntities, List<String> attachementList) throws Exception {

  }
}
