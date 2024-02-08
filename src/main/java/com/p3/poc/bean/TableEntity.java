package com.p3.poc.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TableEntity {
    private String tableName;
    private List<ColumnEntity> columnDetails;
    private  Integer totalRowCount;
    @Builder.Default private Boolean header=true;
    @Builder.Default private Boolean isForeignKeyPresent =false;
    private List<ForeignKeyColumnsInfo> foreignKeyColumnsInfos;
}
