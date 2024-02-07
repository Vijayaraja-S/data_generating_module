package org.example.random_data_generater.controller;

import lombok.RequiredArgsConstructor;
import org.example.random_data_generater.bean.RequestBean;
import org.example.random_data_generater.exception.InvalidInputException;
import org.example.random_data_generater.service.DataGeneratorService;
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
    public  String createData(@RequestBody RequestBean requestBean) throws IOException, ParseException, InvalidInputException {
        dataGeneratorService.createData(requestBean);
        return "Data Generated";
    }
}
