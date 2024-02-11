package com.p3.poc.bean;

import com.p3.export.formatter.ExportFormat;
import com.p3.poc.bean.enums.MappingAssociation;
import com.p3.poc.bean.enums.TypeData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ForeignKeyColumnsInfo {
    private String referenceFilePath;
    private String referenceColumnName;
    private String alterFKeyColumnName;
    private Integer ordinalPosition;
    private TypeData typeData;
    @Builder.Default private Boolean header=true;
    private ExportFormat fileFormat;
    private String delimiter;
    private MappingAssociation mappingAssociation;
}
