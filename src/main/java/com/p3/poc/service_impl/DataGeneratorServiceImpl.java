package com.p3.poc.service_impl;

import com.github.javafaker.Faker;
import com.p3.poc.bean.ForeignKeyColumnsInfo;
import com.p3.poc.bean.TableEntity;
import com.p3.poc.bean.writer.WriterBean;
import com.p3.poc.service.DataGeneratorService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.p3.poc.bean.ColumnEntity;
import com.p3.poc.bean.DataGeneratorBean;
import com.p3.poc.exception.InvalidInputException;

import com.p3.poc.faker.DataProvider;
import com.p3.poc.util.FileUtil;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


@Component
@Slf4j
@RequiredArgsConstructor
public class DataGeneratorServiceImpl implements DataGeneratorService {
    private final ForeignKeyColumnService joinColumnService;
    private final ExportProcess exportProcess;
    private final DataProcessor dataProcessor;



    @Override
    public void createData(DataGeneratorBean requestBean) throws Exception {
        List<WriterBean> writerBeanList = exportProcess.getExportEngineList(requestBean);
        Faker faker = new Faker();
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (TableEntity tableInfo : requestBean.getTableInfos()) {
            WriterBean wBean = writerBeanList.stream().filter(writerBean -> writerBean.getTableName().
                            equalsIgnoreCase(tableInfo.getTableName()))
                    .findFirst().orElse(null);
            if (!executorService.isShutdown()) {
                Callable<Object> callableThreadTask = () -> processTask(tableInfo, tableInfo.getTotalRowCount(), faker, wBean);
            }
        }
        executorService.shutdown();
    }

    private Object processTask(TableEntity tableInfo, Integer totalRowCount, Faker faker, WriterBean wBean) throws Exception {
        return dataProcessor.startDataGeneration(tableInfo,totalRowCount,faker,wBean);
    }

    @Override
    public String beanValidation(DataGeneratorBean requestBean) throws InvalidInputException, IOException {
        String schemaPath = requestBean.getOutputPath() + File.separator + requestBean.getDatabaseName() + File.separator + requestBean.getSchemaName();
        FileUtil.checkCreateDirectory(schemaPath);
        File[] files = new File(schemaPath).listFiles();
        assert files != null;
        if (files.length > 0) {
            List<String> fileList = Arrays.stream(files).map(File::getName).toList();
            for (String fileName : fileList) {
                if (requestBean.getTableInfos().stream().map(TableEntity::getTableName).
                        anyMatch(tableName -> tableName.contains(FilenameUtils.removeExtension(fileName)))) {
                    throw new InvalidInputException
                            (String.format("table is already present in a same schema  %s", FilenameUtils.removeExtension(fileName)));
                }
            }
        }
        for (TableEntity tableInfo : requestBean.getTableInfos()) {
            Set<String> uniqueElements = tableInfo.getColumnDetails().stream()
                    .map(ColumnEntity::getName)
                    .collect(Collectors.toSet());
            List<String> duplicateElements = tableInfo.getColumnDetails().stream().map(ColumnEntity::getName)
                    .filter(n -> !uniqueElements.add(n))
                    .toList();
            if (!duplicateElements.isEmpty()) {
                throw new InvalidInputException(String.format("duplicate columns are present [%s] this table %s", duplicateElements, tableInfo.getTableName()));
            }
        }
        return "validation successfully completed";
    }

}
