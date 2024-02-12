package com.p3.poc.bean.metadata.beans;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CreateMetadata {
    private List<SchemaMetadata> schemas;
}
