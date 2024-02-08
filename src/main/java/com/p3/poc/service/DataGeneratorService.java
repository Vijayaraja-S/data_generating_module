package com.p3.poc.service;

import com.p3.poc.bean.RequestBean;
import com.p3.poc.exception.InvalidInputException;

import java.io.IOException;
import java.text.ParseException;


public interface DataGeneratorService {
    void createData(RequestBean requestBean) throws IOException, ParseException, InvalidInputException;
}
