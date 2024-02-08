package com.p3.export.formatter;

import com.p3.export.exceptions.ExportException;
import com.p3.export.utility.json.JSONArray;
import com.p3.export.utility.json.JSONException;

import java.io.PrintWriter;

public class JsExportHelper extends PlainTextExportHelper {

  public JsExportHelper(final PrintWriter out, final ExportFormat exportFormat) {
    super(out, exportFormat);
  }

  public void write(final JSONArray jsonArray) throws ExportException {
    try {
      jsonArray.write(out);
    } catch (final JSONException e) {
      throw new ExportException("Could not write database", e);
    }
  }
}
