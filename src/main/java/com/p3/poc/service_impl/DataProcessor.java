package com.p3.poc.service_impl;
import com.github.javafaker.Faker;
import com.p3.poc.bean.ColumnEntity;
import com.p3.poc.bean.DataGeneratorBean;
import com.p3.poc.bean.TableEntity;
import com.p3.poc.bean.writer.WriterBean;
import com.p3.poc.faker.DataProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Slf4j
public class DataProcessor implements Runnable {
    @Value("${file.maxSize}")
    private long maxFileSize;
    private final DataProvider dataProvider;
    private final TableEntity tableEntity;
    private Integer rowCount;
    private final WriterBean writerBean;
    private final Faker faker;
    private  final ExportProcess  exportProcess;
    private final DataGeneratorBean requestBean;
    private final Random random = new Random();

    public DataProcessor(DataProvider dataProvider, TableEntity tableEntity, Integer rowCount, WriterBean writerBean, Faker faker, ExportProcess exportProcess, DataGeneratorBean dataGeneratorBean) {
        this.dataProvider = dataProvider;
        this.tableEntity = tableEntity;
        this.rowCount = rowCount;
        this.writerBean = writerBean;
        this.faker = faker;
        this.exportProcess = exportProcess;
        this.requestBean = dataGeneratorBean;
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
            Object[][] objects = preparingRowData(columnsDatum, row);
            if (Files.size(Path.of(writerBean.getExportEngine().getResultPath(false)))>maxFileSize){
                writerBean.getExportEngine().handleDataEnd();
                writerBean.setExportEngine(exportProcess.getExportEngine(requestBean,tableEntity,exportProcess.getColumnInfoList(tableEntity)));
            }
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

        if (Boolean.FALSE.equals(column.getIsPrimaryKey())) {
            blankCount = calculateBlankCount(rowCount, column.getColumnRules().getBlank());
            reoccurrenceCount = calculateReoccurrenceCount(rowCount, column.getColumnRules().getReoccurrence());
            uniqueCount = rowCount - (blankCount + reoccurrenceCount);
        }

        List<Object> columnDatum = generateUniqueData(column, faker, uniqueCount);
        addReoccurrenceData(columnDatum, column, faker, reoccurrenceCount);
        addBlankData(columnDatum, column,blankCount);

        return columnDatum.toArray();
    }

    private int calculateBlankCount(int rowCount, int blankPercentage) {
        return (rowCount * blankPercentage) / 100;
    }

    private int calculateReoccurrenceCount(int rowCount, int reoccurrencePercentage) {
        return (rowCount * reoccurrencePercentage) / 100;
    }

    private List<Object> generateUniqueData(ColumnEntity column, Faker faker, int count) throws ParseException {
        List<Object> columnDatum = new ArrayList<>();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                String data = dataProvider.getData(column.getTypeData(), column, faker);
                columnDatum.add(data);
            }
        }
        return columnDatum;
    }

    private void addReoccurrenceData(List<Object> columnDatum, ColumnEntity column, Faker faker, int count) throws ParseException {
        if (count > 0) {
            if (column.getColumnRules().getReoccurrence() == 100) {
                String data = dataProvider.getData(column.getTypeData(), column, faker);
                for (int i = 0; i < count; i++) {
                    columnDatum.add(data);
                }
            } else {
                for (int i = 0; i < count; i++) {
                    int j = random.nextInt(0, columnDatum.size());
                    columnDatum.add(columnDatum.get(j));
                }
            }
        }
    }

    private void addBlankData(List<Object> columnDatum, ColumnEntity column, int count) {
        if (count > 0) {
            if (column.getColumnRules().getBlank() == 100) {
                for (int i = 0; i < count; i++) {
                    columnDatum.add(null);
                }
            } else {
                for (int i = 0; i < count; i++) {
                    int j = random.nextInt(0, columnDatum.size());
                    columnDatum.add(j, null);
                }
            }
        }
    }

}
