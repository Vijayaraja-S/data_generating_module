package org.example.random_data_generater.bean.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.random_data_generater.bean.DataGeneratorBean;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestBean {
    private List<DataGeneratorBean>dataGeneratorBeans;
    private String outputPath;
    private  Integer rowCount;
    private String outputFormat;
    @Builder.Default private Boolean header=true;
}
