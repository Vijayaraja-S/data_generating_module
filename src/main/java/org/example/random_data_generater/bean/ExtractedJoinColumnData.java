package org.example.random_data_generater.bean;

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
    private List<String> columnDataList = new ArrayList<>();
}
