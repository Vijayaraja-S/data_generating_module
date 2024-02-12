package com.p3.poc.bean.metadata.beans;

import lombok.*;

import java.io.BufferedWriter;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MetadataMonitoring {
    private String filePath;
    private BufferedWriter writerBean;
    private String template;
    private Boolean header;
    private int tableCount;
    private int rollNo;
}
