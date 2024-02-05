package org.example.random_data_generater.service_impl;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.random_data_generater.bean.DataGeneratorBean;
import org.example.random_data_generater.bean.JoinColumnInfo;
import org.example.random_data_generater.bean.RequestBean;
import org.example.random_data_generater.export.CommonWriter;

import org.example.random_data_generater.rule_engine.faker.DataProvider;
import org.example.random_data_generater.service.DataGeneratorService;
import org.example.random_data_generater.writer.WriterManager;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class DataGeneratorServiceImpl implements DataGeneratorService {
    private final DataProvider dataProvider;
    private final WriterManager writerManager;
    private final JoinColumnService joinColumnService;
    private CommonWriter writer;

    @Override
    public void createData(RequestBean requestBean) throws IOException, ParseException {
        if (requestBean.getIsJoinColumn()){
            joinColumnService.prepareJoinColumnData(requestBean.getJoinColumnInfo());
        }
        writer = writerManager.getWriter(requestBean);
        List<String> columns = new ArrayList<>(requestBean.getDataGeneratorBeans().stream()
                .map(DataGeneratorBean::getColumnName)
                .toList());
        if (requestBean.getIsJoinColumn()){
            for (JoinColumnInfo joinColumnInfo : requestBean.getJoinColumnInfo()) {

                columns.add(joinColumnInfo.getAlterColumnName());
            }
        }
        if (requestBean.getHeader()) {
            writer.headerWriter(columns.toArray());
        }
        Faker faker = new Faker();
        Integer rowCount = requestBean.getRowCount();
        int row = 0;
        while (rowCount > 0) {
            if (rowCount > 1000) {
                row = 1000;
            } else {
                row = rowCount;
            }
            rowCount = rowCount - 1000;
            List<Object[]> columnsDatum = new ArrayList<>();
            for (DataGeneratorBean dataGeneratorBean : requestBean.getDataGeneratorBeans()) {
                columnsDatum.add(generatePerColumnRecords(dataGeneratorBean, row, faker));
            }
            if(requestBean.getIsJoinColumn()){
                for (JoinColumnInfo joinColumnInfo : requestBean.getJoinColumnInfo()) {
                    columnsDatum.add(joinColumnService.getColumData(row,joinColumnInfo));
                }
            }
            Object[][] objects = preparingRowData(columnsDatum, row);
            if (writerManager.checkFileSize()){
                writer.close();
                writer = writerManager.getFiles(requestBean);
            }
            for (Object[] object : objects) {
                writer.dataWriter(object);
            }
        }
        writer.close();
    }

    private Object[][] preparingRowData(List<Object[]> columnsDatum, int rows) {
        Object[][] resultArray = new Object[rows][columnsDatum.size()];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columnsDatum.size(); j++) {
                Object[] columData = columnsDatum.get(j);
                resultArray[i][j] = columData[i];
            }
        }
        return resultArray;
    }

    private Object[] generatePerColumnRecords(DataGeneratorBean dataGeneratorBean, Integer rowCount, Faker faker) throws ParseException {
        int blankCount = (rowCount / 100) * dataGeneratorBean.getColumnRules().getBlank();
        int reoccurrenceCount = (rowCount / 100) * dataGeneratorBean.getColumnRules().getReoccurrence();
        int actual_count = rowCount - (blankCount + reoccurrenceCount);
        ArrayList<String> column_datum = new ArrayList<>();
        Random random = new Random();
        // actual count
        if (actual_count>0){
            for (int i = 0; i < actual_count; i++) {
                String data = dataProvider.getData(dataGeneratorBean.getTypeData(), dataGeneratorBean, faker);
                column_datum.add(data);
            }
        }
        // reoccurrence
        if (reoccurrenceCount > 0) {
            if (dataGeneratorBean.getColumnRules().getReoccurrence()==100){
                String data = dataProvider.getData(dataGeneratorBean.getTypeData(), dataGeneratorBean, faker);
                for (int i = 0; i < reoccurrenceCount; i++) {
                    column_datum.add(data);
                }
            }else {
                for (int i = 0; i < reoccurrenceCount; i++) {
                    int j = random.nextInt(0, column_datum.size());
                    column_datum.add(column_datum.get(j));
                }
            }
        }
        // blankCount
        if (blankCount > 0) {
            if (dataGeneratorBean.getColumnRules().getBlank()==100){
                for (int i = 0; i < blankCount; i++) {
                    column_datum.add(null);
                }
            }else {
                for (int i = 0; i < blankCount; i++) {
                    int j = random.nextInt(0, column_datum.size());
                    column_datum.add(j, null);
                }
            }
        }
        return column_datum.toArray();
    }


}
