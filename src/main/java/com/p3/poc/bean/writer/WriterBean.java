package com.p3.poc.bean.writer;

import com.p3.export.operation.ExportEngine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WriterBean {
    private String tableName;
    private ExportEngine exportEngine;
}
