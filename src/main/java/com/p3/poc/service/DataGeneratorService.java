package com.p3.poc.service;

import com.p3.poc.bean.DataGeneratorBean;
import com.p3.poc.exception.InvalidInputException;

import java.io.IOException;
import java.text.ParseException;


public interface DataGeneratorService {
    void createData(DataGeneratorBean requestBean) throws Exception;

    String beanValidation(DataGeneratorBean requestBean) throws InvalidInputException, IOException;
}
