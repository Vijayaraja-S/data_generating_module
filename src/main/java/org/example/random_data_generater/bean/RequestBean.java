package org.example.random_data_generater.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.random_data_generater.export.FileFormat;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestBean {
    private List<DataGeneratorBean>dataGeneratorBeans;
    private String outputPath;
    private  Integer rowCount;
    private FileFormat outputFormat;
    private boolean isJoinColumn;
    private List<JoinColumnInfo> joinColumnInfo;
    @Builder.Default private Boolean header=true;
}
