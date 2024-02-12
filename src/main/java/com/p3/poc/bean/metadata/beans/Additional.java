package com.p3.poc.bean.metadata.beans;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Additional {
    @Builder.Default private Integer minValue = 0;
    @Builder.Default private Integer maxValue = 0;
    @Builder.Default private Integer whiteSpaceCount = 0;
    @Builder.Default private Integer nullCount = 0;
    @Builder.Default private Integer distinctRecord = 0;
    @Builder.Default private List<Object> highFrequencyCharData = new ArrayList<>();
    @Builder.Default private Boolean lengthUniform = false;
    @Builder.Default private Boolean allNumeric = false;

}
