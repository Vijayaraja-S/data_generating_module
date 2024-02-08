package com.p3.poc.service_impl;

import com.github.javafaker.Faker;
import com.p3.poc.bean.ColumnEntity;
import com.p3.poc.bean.ForeignKeyColumnsInfo;
import com.p3.poc.bean.TableEntity;
import com.p3.poc.bean.writer.WriterBean;
import com.p3.poc.faker.DataProvider;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
@Service
@Slf4j
@RequiredArgsConstructor
@Builder
public class DataProcessor {

    private final DataProvider dataProvider;

    public  void startDataGeneration(TableEntity tableInfo, Integer rowCount, Faker faker, WriterBean writerBean) throws Exception {
        int row = 0;
        while (rowCount > 0) {
            if (rowCount > 1000) {
                row = 1000;
            } else {
                row = rowCount;
            }
            rowCount = rowCount - 1000;
            List<Object[]> columnsDatum = new ArrayList<>();
            for (ColumnEntity dataGeneratorBean : tableInfo.getColumnDetails()) {
                columnsDatum.add(generateSingleColumnRecords(dataGeneratorBean, row, faker));
            }
//            if (tableInfo.getIsForeignKeyPresent()) {
//                for (ForeignKeyColumnsInfo FKeyColumn : tableInfo.getForeignKeyColumnsInfos()) {
//                    columnsDatum.add(joinColumnService.getColumData(row, FKeyColumn));
//                }
//            }
            Object[][] objects = preparingRowData(columnsDatum, row);
            for (Object[] object : objects) {
                writerBean.getExportEngine().iterateRows(Arrays.stream(object).toList());
            }
        }
        writerBean.getExportEngine().handleDataEnd();

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

    private Object[] generateSingleColumnRecords(ColumnEntity dataGeneratorBean, Integer rowCount, Faker faker) throws ParseException {
        int blankCount = (rowCount / 100) * dataGeneratorBean.getColumnRules().getBlank();
        int reoccurrenceCount = (rowCount / 100) * dataGeneratorBean.getColumnRules().getReoccurrence();
        int uniqueCount = rowCount - (blankCount + reoccurrenceCount);
        List<Object> column_datum = new ArrayList<>();
        Random random = new Random();
        // actual data
        if (uniqueCount > 0) {
            for (int i = 0; i < uniqueCount; i++) {
                String data = dataProvider.getData(dataGeneratorBean.getTypeData(), dataGeneratorBean, faker);
                column_datum.add(data);
            }
        }
        // reoccurrence data
        if (reoccurrenceCount > 0) {
            if (dataGeneratorBean.getColumnRules().getReoccurrence() == 100) {
                String data = dataProvider.getData(dataGeneratorBean.getTypeData(), dataGeneratorBean, faker);
                for (int i = 0; i < reoccurrenceCount; i++) {
                    column_datum.add(data);
                }
            } else {
                for (int i = 0; i < reoccurrenceCount; i++) {
                    int j = random.nextInt(0, column_datum.size());
                    column_datum.add(column_datum.get(j));
                }
            }
        }
        // blankCount data
        if (blankCount > 0) {
            if (dataGeneratorBean.getColumnRules().getBlank() == 100) {
                for (int i = 0; i < blankCount; i++) {
                    column_datum.add(null);
                }
            } else {
                for (int i = 0; i < blankCount; i++) {
                    int j = random.nextInt(0, column_datum.size());
                    column_datum.add(j, null);
                }
            }
        }
        return column_datum.toArray();
    }
}
