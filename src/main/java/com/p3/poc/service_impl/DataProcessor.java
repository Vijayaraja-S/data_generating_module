package com.p3.poc.service_impl;
import com.github.javafaker.Faker;
import com.p3.poc.bean.ColumnEntity;
import com.p3.poc.bean.TableEntity;
import com.p3.poc.bean.writer.WriterBean;
import com.p3.poc.faker.DataProvider;
import lombok.extern.slf4j.Slf4j;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Slf4j
public class DataProcessor implements Runnable {
    private final DataProvider dataProvider;
    private final TableEntity tableEntity;
    private Integer rowCount;
    private final WriterBean writerBean;
    private final Faker faker;

    public DataProcessor(DataProvider dataProvider, TableEntity tableEntity, Integer rowCount, WriterBean writerBean, Faker faker) {
        this.dataProvider = dataProvider;
        this.tableEntity = tableEntity;
        this.rowCount = rowCount;
        this.writerBean = writerBean;
        this.faker = faker;
    }
    @Override
    public void run() {
        try {
            startDataGeneration();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void startDataGeneration() throws Exception {
        int row = 0;
        while (rowCount > 0) {
            if (rowCount > 1000) {
                row = 1000;
            } else {
                row = rowCount;
            }
            rowCount = rowCount - 1000;
            List<Object[]> columnsDatum = new ArrayList<>();
            for (ColumnEntity dataGeneratorBean : tableEntity.getColumnDetails()) {
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

    private Object[] generateSingleColumnRecords(ColumnEntity column, Integer rowCount, Faker faker) throws ParseException {
        int uniqueCount = rowCount;
        int reoccurrenceCount = 0;
        int blankCount = 0;
        if (!column.getIsPrimaryKey()) {
            blankCount = (rowCount / 100) * column.getColumnRules().getBlank();
            reoccurrenceCount = (rowCount / 100) * column.getColumnRules().getReoccurrence();
            uniqueCount = rowCount - (blankCount + reoccurrenceCount);
        }
        List<Object> column_datum = new ArrayList<>();
        Random random = new Random();
        if (uniqueCount > 0) {
            for (int i = 0; i < uniqueCount; i++) {
                String data = dataProvider.getData(column.getTypeData(), column, faker);
                column_datum.add(data);
            }
        }
        if (reoccurrenceCount > 0) {
            if (column.getColumnRules().getReoccurrence() == 100) {
                String data = dataProvider.getData(column.getTypeData(), column, faker);
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
        if (blankCount > 0) {
            if (column.getColumnRules().getBlank() == 100) {
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
