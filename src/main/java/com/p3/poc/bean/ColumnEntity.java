package com.p3.poc.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.p3.poc.bean.enums.TypeData;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ColumnEntity {
    private String name;
    private Boolean isPrimaryKey;
    private TypeData typeData;
    private ColumnRules columnRules;
    private Integer ordinal;
    private String type;

}

