package com.p3.export.formatter;

import com.p3.export.exceptions.ExportException;
import com.p3.export.utility.json.JSONArray;
import com.p3.export.utility.json.JSONException;

import java.io.PrintWriter;

public class JsonExportHelper extends PlainTextExportHelper {

  public JsonExportHelper(final PrintWriter out, final ExportFormat exportFormat) {
    super(out, exportFormat);
  }

  public void write(final JSONArray jsonArray) throws ExportException {
    try {
      jsonArray.write(out, 2, 0);
    } catch (final JSONException e) {
      throw new ExportException("Could not write database", e);
    }
  }
}
