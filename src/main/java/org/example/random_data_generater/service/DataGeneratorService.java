package org.example.random_data_generater.service;

import org.example.random_data_generater.bean.RequestBean;

import java.io.IOException;
import java.text.ParseException;


public interface DataGeneratorService {
    void createData(RequestBean requestBean) throws IOException, ParseException;
}
