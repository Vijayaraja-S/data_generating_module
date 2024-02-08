package com.p3.export.formatter;

import com.p3.export.specifics.BlobKeySplitBean;
import com.p3.export.specifics.DataType;
import com.p3.export.specifics.ExcelSpecificDataType;
import com.p3.export.utility.html.Alignment;
import com.p3.export.utility.html.TableCell;
import com.p3.export.utility.html.TableHeaderCell;
import com.p3.export.utility.html.TableRow;
import com.p3.export.utility.others.Color;

import java.io.PrintWriter;
import java.util.List;

/** Methods to format entire rows of output as HTML. */
abstract class BaseTextExportHelper implements TextExportHelper {
  protected final PrintWriter out;
  private final ExportFormat exportFormat;

  public BaseTextExportHelper(final PrintWriter out, final ExportFormat exportFormat) {
    this.out = out;
    this.exportFormat = exportFormat;
  }

  static String separator(final String pattern) {
    final StringBuilder dashedSeparator = new StringBuilder(72);
    for (int i = 0; i < 72 / pattern.length(); i++) {
      dashedSeparator.append(pattern);
    }
    return dashedSeparator.toString();
  }

  @Override
  public TextExportHelper append(final String text) {
    out.write(text);
    out.flush();
    return this;
  }

  @Override
  public void writeEmptyRow() {
    final TableRow tableRow = new TableRow(exportFormat);
    tableRow.add(
        new TableCell("", true, 0, Alignment.inherit, false, "", Color.white, 3, exportFormat));
    out.println(tableRow);
  }

  @Override
  public void writeRow(
      List<Object> columnData,
      List<ExcelSpecificDataType> excelSpecificDataTypes,
      List<DataType> dataTypes) {
    ExportFormat exportFormat = this.exportFormat;
    if (exportFormat == ExportFormat.txt) {
      exportFormat = ExportFormat.tsv;
    }
    final TableRow row = new TableRow(exportFormat);
    for (final Object element : columnData) {
      if (element == null) {
        row.add(newTableCell(null, "data_null", exportFormat));
      } else if (element instanceof BlobKeySplitBean) {
        final BlobKeySplitBean blobKeySplitBean = (BlobKeySplitBean) element;
        if (blobKeySplitBean.getError() == null) {
          final TableCell cell =
              newTableCell(
                  (exportFormat == ExportFormat.html
                      ? "<a href =\""
                          + blobKeySplitBean.getRelativePath()
                          + "\">"
                          + blobKeySplitBean.getOutputFileName()
                          + "</a>"
                      : blobKeySplitBean.getOutputFileName()
                          + " (PATH : "
                          + blobKeySplitBean.getRelativePath()
                          + ")"),
                  "",
                  exportFormat,
                  exportFormat != ExportFormat.html);
          row.add(cell);
        } else {
          row.add(
              newTableCell(
                  blobKeySplitBean.getOutputFileName()
                      + (exportFormat == ExportFormat.html
                          ? " <span class=\"cursor-pointer\" title=\""
                              + blobKeySplitBean.getError()
                              + "\">⚠️</span>"
                          : " (MESSAGE : " + blobKeySplitBean.getError())
                      + ")",
                  "blob_error",
                  exportFormat,
                  exportFormat != ExportFormat.html));
        }
      } else if (element instanceof Number) {
        row.add(newTableCell(element.toString(), "data_number", exportFormat));
      } else row.add(newTableCell(element.toString(), "", exportFormat));
    }
    out.println(row);
  }

  @Override
  public void writeRowHeader(List<String> columnNames) {
    ExportFormat exportFormat = this.exportFormat;
    // if (outputFormat == TextOutputFormat.text) outputFormat = TextOutputFormat.txt;
    if (exportFormat == ExportFormat.txt) {
      exportFormat = ExportFormat.tsv;
    }
    final TableRow row = new TableRow(exportFormat);
    for (final String columnName : columnNames) {
      final TableHeaderCell headerCell =
          new TableHeaderCell(
              columnName, true, 0, Alignment.inherit, true, "", Color.white, 1, exportFormat);
      row.add(headerCell);
    }

    out.println(row);
  }

  private TableCell newTableCell(
      final String text, final String styleClass, final ExportFormat exportFormat) {
    return newTableCell(text, styleClass, exportFormat, true);
  }

  private TableCell newTableCell(
      final String text,
      final String styleClass,
      final ExportFormat exportFormat,
      boolean escapeText) {
    return new TableCell(
        text, escapeText, 0, Alignment.inherit, false, styleClass, Color.white, 1, exportFormat);
  }
}
