package com.p3.export.formatter;

import com.p3.export.specifics.DataType;
import com.p3.export.specifics.ExcelSpecificDataType;
import com.p3.export.utility.excel.ExcelEngine;
import com.p3.poc.bean.ColumnEntity;


import java.io.File;
import java.util.List;

public class ExcelExportHelper implements TextExportHelper {

  protected final File out;
  private final ExportFormat exportFormat;
  private final String title;
  private final ExcelEngine excelEngineHandler;

  public ExcelExportHelper(File out, ExportFormat exportFormat, String title) {
    this.out = out;
    this.exportFormat = exportFormat;
    this.title = title;
    excelEngineHandler = new ExcelEngine(out);
  }

  @Override
  public TextExportHelper append(String text) {
    return null;
  }

  @Override
  public void writeDocumentEnd() throws Exception {
    excelEngineHandler.writeExcel();
  }

  @Override
  public void writeDocumentStart() throws Exception {
    excelEngineHandler.createWorkBook();
    excelEngineHandler.createSheet(title);
  }

  @Override
  public void writeEmptyRow() throws Exception {
    excelEngineHandler.createNewRow();
  }

  @Override
  public void writeRow(
      List<Object> columnData,
      List<ExcelSpecificDataType> excelSpecificDataTypes,
      List<DataType> dataTypes)
      throws Exception {
    excelEngineHandler.addRecordRow(columnData, excelSpecificDataTypes);
  }

  @Override
  public void writeRowHeader(List<String> columnNames) throws Exception {
    excelEngineHandler.addTitlesRow(columnNames);
  }

  @Override
  public void flush() {}

  @Override
  public void close() {}

  public void writeRow(List<Object> currentRow, List<ExcelSpecificDataType> excelSpecificDataTypes, List<ColumnEntity> columnEntities, List<String> attachementList) throws Exception {

  }
}
