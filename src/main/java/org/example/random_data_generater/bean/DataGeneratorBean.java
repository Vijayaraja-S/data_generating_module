package org.example.random_data_generater.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DataGeneratorBean {
    private String columnName;
    private ColumnDataInfo columnInfo;
    private TypeData typeData;
    @Builder.Default private Boolean unique=false;
    @Builder.Default private int reoccurrence = 0;
    @Builder.Default private int blank = 0;
}
