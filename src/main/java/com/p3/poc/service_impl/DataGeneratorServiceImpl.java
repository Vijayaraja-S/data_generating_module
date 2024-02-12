package com.p3.poc.service_impl;

import com.github.javafaker.Faker;
import com.p3.poc.bean.TableEntity;
import com.p3.poc.bean.enums.TypeData;
import com.p3.poc.bean.writer.WriterBean;
import com.p3.poc.faker.DataProvider;
import com.p3.poc.service.DataGeneratorService;

import com.p3.poc.service_impl.metadata.MetaDataExtraction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.p3.poc.bean.ColumnEntity;
import com.p3.poc.bean.DataGeneratorBean;
import com.p3.poc.exception.InvalidInputException;


import com.p3.poc.util.FileUtil;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;


import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Component
@Slf4j
@RequiredArgsConstructor
public class DataGeneratorServiceImpl implements DataGeneratorService {
    private final ExportProcess exportProcess;
    private final DataProvider dataProvider;
    private final MetaDataExtraction metaDataExtraction;
    @Override
    public void createData(DataGeneratorBean requestBean) throws Exception {
        List<WriterBean> writerBeanList = exportProcess.getExportEngineList(requestBean);
        Faker faker = new Faker();
        List<Callable<Object>> callableList = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        metaDataExtraction.schemaMetadataUpdate(requestBean.getSchemaName(),requestBean.getDatabaseName(),requestBean.getTableInfos().size(),requestBean.getOutputPath());
        for (TableEntity tableInfo : requestBean.getTableInfos()) {
            metaDataExtraction.TableMetadataUpdate(tableInfo.getTableName(),requestBean.getSchemaName(),tableInfo.getColumnDetails(),requestBean.getDatabaseName(),requestBean.getOutputPath(),tableInfo.getTotalRowCount());
            WriterBean wBean = writerBeanList.stream()
                    .filter(writerBean -> writerBean.getTableName().equalsIgnoreCase(tableInfo.getTableName()))
                    .findFirst()
                    .orElse(null);
            callableList.add(Executors.callable(new DataProcessor(dataProvider, tableInfo, tableInfo.getTotalRowCount(), wBean, faker,exportProcess,requestBean)));
        }
        if (!executorService.isShutdown()) {
            executorService.invokeAll(callableList);
        }
        executorService.shutdown();
    }

    @Override
    public String beanValidation(DataGeneratorBean requestBean) throws InvalidInputException, IOException {
        String schemaPath = requestBean.getOutputPath() + File.separator + requestBean.getDatabaseName() + File.separator + requestBean.getSchemaName();
        FileUtil.checkCreateDirectory(schemaPath);
        File[] files = new File(schemaPath).listFiles();
        assert files != null;
        // table validation
        if (files.length > 0) {
            List<String> fileList = Arrays.stream(files).map(File::getName).toList();
            for (String fileName : fileList) {
                if (requestBean.getTableInfos().stream().map(TableEntity::getTableName).
                        anyMatch(tableName -> tableName.contains(FilenameUtils.removeExtension(fileName)))) {
                    throw new InvalidInputException
                            (String.format("table is already present in a same schema  %s", FilenameUtils.removeExtension(fileName)));
                }
                if (requestBean.getTableInfos().size() > 20) {
                    throw new InvalidInputException(String.format("table count is exceed %s", requestBean.getTableInfos().size()));
                }
            }
        }
        for (TableEntity tableInfo : requestBean.getTableInfos()) {
            // column Name validation
            Set<String> uniqueElements = new HashSet<>();
            List<String> duplicateElements = tableInfo.getColumnDetails().stream().map(ColumnEntity::getName)
                    .filter(n -> !uniqueElements.add(n))
                    .toList();
            if (!duplicateElements.isEmpty()) {
                throw new InvalidInputException(String.format("duplicate columns are present %s - tableName: %s"
                        ,duplicateElements, tableInfo.getTableName()));
            }
            uniqueElements.clear();
            // column ordinal validation
            Set<Integer> uniqueOrdinal = new HashSet<>();
            List<Integer> duplicateOrdinal = tableInfo.getColumnDetails().stream().map(ColumnEntity::getOrdinal)
                    .filter(ordinal -> !uniqueOrdinal.add(ordinal)||ordinal == 0)
                    .toList();
            if (!duplicateOrdinal.isEmpty()) {
                throw new InvalidInputException(String.format("duplicate ordinal are present[ordinal should not be 0] %s - tableName: %s"
                        , duplicateOrdinal, tableInfo.getTableName()));
            }
            //size of column
            if (tableInfo.getColumnDetails().size() > 30) {
                throw new InvalidInputException(String.format("column count is exceed %s tableName:%s",
                        tableInfo.getColumnDetails().size(), tableInfo.getTableName()));
            }
            // TypeData input
            List<ColumnEntity> dateColumn = tableInfo.getColumnDetails().stream()
                    .filter(columnEntity -> columnEntity.getTypeData().equals(TypeData.DATE))
                    .toList();
            if (!dateColumn.isEmpty()){
                for (ColumnEntity column : dateColumn) {
                    if (column.getTypeData().equals(TypeData.DATE)){
                        if (column.getColumnRules().getFromDate().isEmpty()||column.getColumnRules().getToDate().isEmpty()){
                            throw new InvalidInputException(String.format("missing from date to date %s tableName:%s",
                                    column.getName(), tableInfo.getTableName()));
                        }
                    }
                }
            }
        }
        return "validation successfully completed";
    }
}
