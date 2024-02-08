package com.p3.poc.bean;

import com.p3.poc.export.FileFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestBean {
    private List<ColumnEntity> columnDetails;
    private String outputPath;
    private  Integer rowCount;
    private FileFormat outputFormat;
    private String databaseName;
    private String schemaName;
    private String tableName;
    @Builder.Default private Boolean isJoinColumn=false;
    private List<JoinColumnInfo> joinColumnInfo;
    @Builder.Default private Boolean header=true;
}
