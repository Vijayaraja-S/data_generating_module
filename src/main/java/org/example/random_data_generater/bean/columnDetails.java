package org.example.random_data_generater.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.random_data_generater.bean.enums.TypeData;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class columnDetails {
    private String columnName;
    private ColumnRules columnRules;
    private TypeData typeData;
}
//1.column name validation more than once or not .

