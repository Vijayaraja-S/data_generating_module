package com.p3.poc.service_impl;

import com.p3.export.operation.ExportEngine;
import com.p3.export.options.ColumnInfo;
import com.p3.poc.bean.ColumnEntity;
import com.p3.poc.bean.DataGeneratorBean;
import com.p3.poc.bean.ForeignKeyColumnsInfo;
import com.p3.poc.bean.TableEntity;
import com.p3.poc.bean.writer.WriterBean;
import com.p3.poc.util.FileUtil;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExportProcess {

    public List<WriterBean> getExportEngineList(DataGeneratorBean requestBean) throws Exception {
        List<WriterBean> writerBeans = new ArrayList<>();
        for (TableEntity tableInfo : requestBean.getTableInfos()) {
            List<ColumnInfo> prepareColumnInfolist = getColumnInfoList(tableInfo);
            writerBeans.add(
                    WriterBean.builder()
                    .tableName(tableInfo.getTableName())
                    .exportEngine(getExportEngine(requestBean, tableInfo, prepareColumnInfolist))
                    .build()
            );
        }
        return writerBeans;
    }

    public ExportEngine getExportEngine(DataGeneratorBean requestBean, TableEntity tableInfo, List<ColumnInfo> prepareColumnInfolist) throws Exception {
        ExportEngine ee = ExportEngine.builder()
                .exportFormat(requestBean.getOutputFormat())
                .basePath(getBasePath(requestBean, tableInfo.getTableName()))
                .columnsInfo(prepareColumnInfolist)
                .title(requestBean.getSchemaName() + "-" + tableInfo.getTableName())
                .build();
        ee.initialize();
        ee.handleDataStart();
        return ee;
    }

    public List<ColumnInfo> getColumnInfoList(TableEntity tableInfo) {
        List<ColumnInfo> prepareColumnInfolist;
        // check foreignKey columns present or not
        if (Boolean.TRUE.equals(tableInfo.getIsForeignKeyPresent())) {
            prepareColumnInfolist = prepareColumInfoList(tableInfo.getColumnDetails(),
                    tableInfo.getForeignKeyColumnsInfos());
        }else{
            prepareColumnInfolist = prepareColumInfoList(tableInfo.getColumnDetails());
        }
        return prepareColumnInfolist;
    }

    private String getBasePath(DataGeneratorBean requestBean, String tableName) throws IOException {
        String folderPath =  requestBean.getOutputPath() + File.separator + requestBean.getDatabaseName()
                + File.separator + requestBean.getSchemaName() + File.separator + tableName;
        FileUtil.checkCreateDirectory(folderPath);
        return folderPath;
    }

    public List<ColumnInfo> prepareColumInfoList(List<ColumnEntity> columnDetails) {
        List<ColumnInfo> columnInfos = new ArrayList<>();
        for (ColumnEntity column : columnDetails) {
            columnInfos.add(ColumnInfo.builder()
                    .column(column.getName())
                    .dataType(column.getTypeData().getDataType())
                    .ordinalPosition(column.getOrdinal())
                    .build());
        }
        return columnInfos;
    }

    private List<ColumnInfo> prepareColumInfoList(List<ColumnEntity> columnDetails,
                                                  List<ForeignKeyColumnsInfo> foreignKeyColumnsInfos) {
        List<ColumnInfo> columnInfos = new ArrayList<>();
        for (ColumnEntity column : columnDetails) {
            columnInfos.add(ColumnInfo.builder()
                    .column(column.getName())
                    .dataType(column.getTypeData().getDataType())
                    .ordinalPosition(column.getOrdinal())
                    .build());
        }
        for (ForeignKeyColumnsInfo fColumn : foreignKeyColumnsInfos) {
            columnInfos.add(
                    ColumnInfo.builder()
                            .column(fColumn.getAlterFKeyColumnName())
                            .ordinalPosition(fColumn.getOrdinalPosition())
                            .dataType(fColumn.getTypeData().getDataType())
                            .build());
        }
        return columnInfos;
    }


}
