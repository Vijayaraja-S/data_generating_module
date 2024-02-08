package com.p3.export.formatter;

import com.p3.export.specifics.ExcelSpecificDataType;
import com.p3.export.utility.others.Utility;
import com.p3.poc.bean.ColumnEntity;


import java.io.PrintWriter;
import java.util.List;

/**
 * Methods to format entire rows of output as HTML.
 *
 * @author Sualeh Fatehi
 */
public final class HtmlExportHelper extends BaseTextExportHelper {

  private static final String HTML_HEADER = htmlHeader("Archon - HTML Output");
  private static final String HTML_FOOTER =
      "</table><p>&#160;</p></body>" + System.lineSeparator() + "</html>";

  public HtmlExportHelper(final PrintWriter out, final ExportFormat exportFormat) {
    super(out, exportFormat);
  }

  private static String htmlHeader(String title) {
    final StringBuilder styleSheet = new StringBuilder(4096);
    styleSheet
        .append(System.lineSeparator())
        .append(Utility.readResourceFully("/sc.css"))
        .append(System.lineSeparator())
        .append(Utility.readResourceFully("/sc_output.css"))
        .append(System.lineSeparator());

    return "<!DOCTYPE html>"
        + System.lineSeparator()
        + "<html lang=\"en\">"
        + System.lineSeparator()
        + "<head>"
        + System.lineSeparator()
        + "  <title>"
        + title
        + "</title>"
        + System.lineSeparator()
        + "  <meta charset=\"utf-8\"/>"
        + System.lineSeparator()
        + "  <style>"
        + styleSheet
        + "  </style>"
        + System.lineSeparator()
        + "</head>"
        + System.lineSeparator()
        + "<body>"
        + System.lineSeparator()
        + "<table>"
        + System.lineSeparator();
  }

  @Override
  public void writeDocumentEnd() {
    out.println(HTML_FOOTER);
  }

  @Override
  public void writeDocumentStart() {
    out.println(HTML_HEADER);
  }

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
