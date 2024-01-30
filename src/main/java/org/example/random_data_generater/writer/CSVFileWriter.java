package org.example.random_data_generater.writer;

import com.opencsv.CSVWriter;
import org.example.random_data_generater.export.CommonWriter;


import java.io.BufferedWriter;
import java.io.IOException;

public class CSVFileWriter implements CommonWriter {
    private   CSVWriter csvWriter;

    public CSVFileWriter(BufferedWriter writer) {
        this.csvWriter = new CSVWriter(writer);
    }

    @Override
    public void HeaderWriter(String[] columns) throws IOException {
        csvWriter.writeNext(columns);
        csvWriter.flush();
    }
    @Override
    public void DataWriter(String[] datum) {

    }
}
