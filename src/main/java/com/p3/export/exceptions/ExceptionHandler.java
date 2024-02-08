package com.p3.export.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;


public class ExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandler.class);


    public static String exception(String errorMessage, Exception ex) {
        Writer buffer = new StringWriter();
        PrintWriter pw = new PrintWriter(buffer);
        ex.printStackTrace(pw);
        LOGGER.error(buffer.toString());
        return buffer.toString();
    }

}
