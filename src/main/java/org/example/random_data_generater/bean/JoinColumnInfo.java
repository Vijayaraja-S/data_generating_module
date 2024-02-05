package org.example.random_data_generater.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.random_data_generater.export.FileFormat;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JoinColumnInfo {
    private String filePath;
    private String columnName;
    private String alterColumnName;
    private Integer ordinalPosition;
    private Boolean isHeader;
    private FileFormat fileFormat;
    private String delimiter;
}
