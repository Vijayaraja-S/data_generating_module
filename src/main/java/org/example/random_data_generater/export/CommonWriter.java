package org.example.random_data_generater.export;

import java.io.IOException;

public interface CommonWriter {
    void HeaderWriter(String[] columns) throws IOException;
    void DataWriter(String[] datum);
}
