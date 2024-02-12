package com.p3.poc.bean.metadata.beans;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class FieldProperties {
    @Builder.Default private Integer precision = 5;
    @Builder.Default private Integer scale = 0;
    @Builder.Default private Boolean nullable = false;
    @Builder.Default private Boolean autoIncrement = false;
    @Builder.Default private Boolean primaryKey = false;
}
