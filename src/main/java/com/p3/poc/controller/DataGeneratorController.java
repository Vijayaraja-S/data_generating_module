package com.p3.poc.controller;

import lombok.RequiredArgsConstructor;
import com.p3.poc.bean.DataGeneratorBean;
import com.p3.poc.exception.InvalidInputException;
import com.p3.poc.service.DataGeneratorService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;

@RestController
@RequestMapping("/data_generator")
@RequiredArgsConstructor
public class DataGeneratorController {
    private final DataGeneratorService dataGeneratorService;
    @PostMapping
    public  String createData(@RequestBody DataGeneratorBean requestBean) throws Exception {
        dataGeneratorService.createData(requestBean);
        return "Data Generated";
    }
    @PostMapping
    public String inputValidation(@RequestBody DataGeneratorBean requestBean) throws IOException, ParseException, InvalidInputException {
        return dataGeneratorService.beanValidation(requestBean);
    }
}
