package org.example.random_data_generater.service_impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.example.random_data_generater.bean.ExtractedJoinColumnData;
import org.example.random_data_generater.bean.JoinColumnInfo;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Component
public class JoinColumnService {

    private Map<String,CSVParser> columnParserMap = new HashMap<>();
    private Map<String,Integer> offsetTracker = new HashMap<>() ;

    public Map<String, ExtractedJoinColumnData> prepareJoinColumnData(List<JoinColumnInfo> joinColumnInfos) throws IOException {
        Map<String, ExtractedJoinColumnData> joinColumnData = new HashMap<>();
        int ordinalPosition;
        for (JoinColumnInfo joinColumnInfo : joinColumnInfos) {
            if (joinColumnInfo.getOrdinalPosition() != null) {
                ordinalPosition = joinColumnInfo.getOrdinalPosition();
                ExtractedJoinColumnData csvColumnData = getCSVColumnData(joinColumnInfo, ordinalPosition);
                joinColumnData.put(joinColumnInfo.getColumnName(),csvColumnData);
            } else {
                if (joinColumnInfo.isHeader()) {
                    if (!joinColumnInfo.getColumnName().isEmpty()) {
                        ordinalPosition = fetchOrdinalPosition(joinColumnInfo.getColumnName(), joinColumnInfo.getFilePath());
                        ExtractedJoinColumnData csvColumnData = getCSVColumnData(joinColumnInfo, ordinalPosition);
                        joinColumnData.put(joinColumnInfo.getColumnName(),csvColumnData);
                    }
                    else
                        throw new IllegalArgumentException("provide column name ");
                }else
                    throw new IllegalArgumentException("provide column name or ordinal position");
            }
        }
        return joinColumnData;
    }
    public void getParserAllFiles(List<JoinColumnInfo>joinColumnInfos) throws IOException {
        for (JoinColumnInfo joinColumnInfo : joinColumnInfos) {
            // check format before parse
            offsetTracker.put(joinColumnInfo.getColumnName(),0);
            columnParserMap.put(joinColumnInfo.getColumnName(), getCsvParser(joinColumnInfo.getFilePath()));
        }
    }

    private CSVParser getCsvParser(String filePath) throws IOException {
            return new CSVParser(new FileReader(filePath), CSVFormat.DEFAULT);
    }

    private ExtractedJoinColumnData getCSVColumnData(JoinColumnInfo joinColumnInfo, int ordinalPosition) {
        try(CSVParser parser = new CSVParser(new FileReader(joinColumnInfo.getFilePath()), CSVFormat.DEFAULT)){
            int rowCount = 0;
            ExtractedJoinColumnData extractedBean = ExtractedJoinColumnData.builder().build();
            List<String> columData = new ArrayList<>();
            for (CSVRecord record : parser) {rowCount++;
                if (columData.size()>999){
                    extractedBean.getColumnDataList().add(columData.toArray());
                    columData.clear();
                }
                columData.add(record.get(ordinalPosition));
            }
            extractedBean.setRowCount(rowCount);
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
            return columnIndex;
        }
    }

    public Object[] generateColumnRecord(int row, JoinColumnInfo joinColumnInfo) throws IOException {
        int ordinalPosition;
        if (joinColumnInfo.getOrdinalPosition() != null) {
            ordinalPosition = joinColumnInfo.getOrdinalPosition();
            return fetchCSVColumnData(joinColumnInfo, ordinalPosition,row);
        } else {
            if (joinColumnInfo.isHeader()) {
                if (!joinColumnInfo.getColumnName().isEmpty()) {
                    ordinalPosition = fetchOrdinalPosition(joinColumnInfo.getColumnName(), joinColumnInfo.getFilePath());
                   return fetchCSVColumnData(joinColumnInfo, ordinalPosition,row);
                }
                else
                    throw new IllegalArgumentException("provide column name ");
            }else
                throw new IllegalArgumentException("provide column name or ordinal position");
        }

    }

    private Object[] fetchCSVColumnData(JoinColumnInfo joinColumnInfo, int ordinalPosition, int row) {
        CSVParser parser = columnParserMap.get(joinColumnInfo.getColumnName());

        for (CSVRecord record : parser) {
            String value = record.get(ordinalPosition);
        }

    }
}
