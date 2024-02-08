package com.p3.poc.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExtractedJoinColumnData {
    @Builder.Default private List<String> columnDataList = new ArrayList<>();
}
