package com.p3.poc.bean.metadata.beans;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class DataProperties {
    @Builder.Default private Boolean analysed = false;
    private Additional additional;
}

