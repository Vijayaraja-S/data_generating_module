package org.example.random_data_generater.service_impl;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.random_data_generater.bean.DTO.RequestBean;
import org.example.random_data_generater.bean.DataGeneratorBean;
import org.example.random_data_generater.export.CommonWriter;

import org.example.random_data_generater.rule_engine.faker.DataProvider;
import org.example.random_data_generater.service.DataGeneratorService;
import org.example.random_data_generater.util.FileUtil;
import org.example.random_data_generater.writer.CSVFileWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class DataGeneratorServiceImpl implements DataGeneratorService {
    private final DataProvider dataProvider;
    private CommonWriter writer;


    @Override
    public void createData(RequestBean requestBean) throws IOException {
        writer = getWriter(requestBean);
        String [] columns = (String[]) requestBean.getDataGeneratorBeans().stream()
                .map(DataGeneratorBean::getColumnName)
                .toArray();
        if (requestBean.getHeader()){
            writer.HeaderWriter(columns);
        }
        Integer rowCount = requestBean.getRowCount();
        for (DataGeneratorBean dataGeneratorBean : requestBean.getDataGeneratorBeans()) {
            generatePerColumnRecords(dataGeneratorBean,rowCount);
        }
    }
    public CommonWriter getWriter(RequestBean inputBean) throws IOException {
        String outputPath = getOutputPath(inputBean);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath));
            return switch (inputBean.getOutputFormat().toUpperCase()) {
                case "CSV" -> new CSVFileWriter(writer);
                case "TXT" -> null;
                default -> null;
            };
        }catch (IOException e){
            log.error("unable to create a writer");
        }
        return null;
    }

    private void generatePerColumnRecords(DataGeneratorBean dataGeneratorBean, Integer rowCount) {
        int count=0;
        while (rowCount > count){
            count++;
            int blankCount = (rowCount / 100)*dataGeneratorBean.getBlank();
            int reoccurrenceCount = (rowCount / 100) * dataGeneratorBean.getReoccurrence();
            String data = dataProvider.getData(dataGeneratorBean.getTypeData(),dataGeneratorBean);
        }
    }
    private String getOutputPath(RequestBean inputBean) throws IOException {
        Date startTime = new Date();
        String jobFolder = inputBean.getOutputPath() + File.separator + "DUMMY_DATA_" + startTime.getTime();
        FileUtil.checkCreateDirectory(jobFolder);
        return jobFolder + File.separator + "DUMMY_DATA" + UUID.randomUUID() + ".csv";
    }



}
