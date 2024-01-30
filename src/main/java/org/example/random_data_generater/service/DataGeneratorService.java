package org.example.random_data_generater.service;

import org.example.random_data_generater.bean.DTO.RequestBean;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface DataGeneratorService {
    void createData(RequestBean requestBean) throws IOException;
}
