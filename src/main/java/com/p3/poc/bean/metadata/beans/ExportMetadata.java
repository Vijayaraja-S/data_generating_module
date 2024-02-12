package com.p3.poc.bean.metadata.beans;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ExportMetadata {
    // asd metadata bean should validate - (build creation)
    private CreateMetadata metadata;
}
