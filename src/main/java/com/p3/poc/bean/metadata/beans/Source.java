package com.p3.poc.bean.metadata.beans;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Source {
    @Builder.Default private String origin = "";
    @Builder.Default private String name = "";
    @Builder.Default private Integer typeLength = 0;
    @Builder.Default private String type = "";
    @Builder.Default private Boolean index = false;
}

