package com.p3.poc.bean.metadata.beans;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SchemaMetadata implements Serializable {
    private String name;
    private String sourceDatabaseName;
    private Integer  tableCount;
    @Builder.Default private String description = "";
    private List<TableMetadata> tables;
    @Builder.Default private String sourceName = "info-Archive";
}
