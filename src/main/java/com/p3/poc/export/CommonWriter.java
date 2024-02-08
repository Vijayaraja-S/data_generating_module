package com.p3.poc.export;

import java.io.IOException;

public interface CommonWriter {
    void headerWriter(Object[] columns) throws IOException;
    void dataWriter(Object[] datum) throws IOException;
    void close() throws IOException;
}
