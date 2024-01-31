package org.example.random_data_generater.writer;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.example.random_data_generater.export.CommonWriter;


import java.io.BufferedWriter;
import java.io.IOException;

public class CSVFileWriter implements CommonWriter {
    private   CSVPrinter  csvWriter;
    public CSVFileWriter(BufferedWriter writer) throws IOException {
        this.csvWriter = new CSVPrinter(writer, CSVFormat.DEFAULT);
    }
    @Override
    public void headerWriter(Object[] columns) throws IOException {
        csvWriter.printRecord(columns);
    }
    @Override
    public void dataWriter(Object[] datum) throws IOException {
        csvWriter.printRecord(datum);
    }
    @Override
    public void close() throws IOException {
        csvWriter.flush();
        csvWriter.close();
    }
}
