package com.p3.poc.service_impl;

import com.p3.poc.bean.ForeignKeyColumnsInfo;
import com.p3.poc.exception.InvalidInputException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import com.p3.poc.bean.ExtractedJoinColumnData;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Component
@Slf4j
public class ForeignKeyColumnService {
    Map<String, ExtractedJoinColumnData> joinColumnData = new HashMap<>();

    public void prepareJoinColumnData(List<ForeignKeyColumnsInfo> joinColumnInfos) throws IOException {
        int ordinalPosition;
        for (ForeignKeyColumnsInfo joinColumnInfo : joinColumnInfos) {
            if (joinColumnInfo.getOrdinalPosition() != null) {
                ordinalPosition = joinColumnInfo.getOrdinalPosition();
                ExtractedJoinColumnData csvColumnData = getCSVColumnData(joinColumnInfo, ordinalPosition);
                joinColumnData.put(joinColumnInfo.getReferenceColumnName(),csvColumnData);
            } else {
//                if (joinColumnInfo.getIsHeader()) {
//                    if (!joinColumnInfo.getReferenceColumnName().isEmpty()) {
//                        ordinalPosition = fetchOrdinalPosition(joinColumnInfo.getReferenceColumnName(), joinColumnInfo.getReferenceFilePath());
//                        ExtractedJoinColumnData csvColumnData = getCSVColumnData(joinColumnInfo, ordinalPosition);
//                        joinColumnData.put(joinColumnInfo.getReferenceColumnName(),csvColumnData);
//                    }
//                    else{
//                        log.error("provide column name");
//                        throw new IllegalArgumentException("provide column name ");
////                    }
//                }else{
//                    log.error("provide column name or ordinal position");
//                    throw new IllegalArgumentException("provide column name or ordinal position");
//                }
            }
        }
    }

    private ExtractedJoinColumnData getCSVColumnData(ForeignKeyColumnsInfo joinColumnInfo, int ordinalPosition) {
        try(CSVParser parser = new CSVParser(new FileReader(joinColumnInfo.getReferenceFilePath()), CSVFormat.DEFAULT)){
            ExtractedJoinColumnData extractedBean = ExtractedJoinColumnData.builder().build();
            int row=0;
            ArrayList<String> objects = new ArrayList<>();
            for (CSVRecord record : parser) {
                row++;
                String value = String.valueOf(record.get(ordinalPosition));
                if (!StringUtils.isBlank(value) && row>1){
                    objects.add(value);
                }
                if (objects.size()>10000){
                    break;
                }
            }
            parser.close();
            extractedBean.setColumnDataList(objects);
            return extractedBean;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int fetchOrdinalPosition(String columnName, String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String[] columnNames = reader.readLine().split(",");
            int columnIndex = -1;
            for (int i = 0; i < columnNames.length; i++) {
                if (columnNames[i].equalsIgnoreCase(columnName)) {
                    columnIndex = i;
                    break;
                }
            }
            if (columnIndex==-1){
                log.error("column does not found.");
                throw new InvalidInputException("column does not found.");
            }
            return columnIndex;
        } catch (InvalidInputException e) {
            throw new RuntimeException(e);
        }
    }

    public Object[] getColumData(int row, ForeignKeyColumnsInfo joinColumnInfo) {
        Random random = new Random();
        ExtractedJoinColumnData extractedJoinColumnData = joinColumnData.get(joinColumnInfo.getReferenceColumnName());
        List<String> columnDatum = new ArrayList<>();
        for (int i = 0; i <  row; i++) {
            int j = random.nextInt(0, extractedJoinColumnData.getColumnDataList().size());
            columnDatum.add(extractedJoinColumnData.getColumnDataList().get(j));
        }
        return columnDatum.toArray();
    }
}
