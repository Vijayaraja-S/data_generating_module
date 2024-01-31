package org.example.random_data_generater.export;

import java.io.IOException;

public interface CommonWriter {
    void headerWriter(Object[] columns) throws IOException;
    void dataWriter(Object[] datum) throws IOException;
    void close() throws IOException;
}
