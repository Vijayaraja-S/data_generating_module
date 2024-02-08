package com.p3.export.options;

import com.p3.export.specifics.DataType;
import com.p3.export.specifics.ExcelSpecificDataType;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ColumnInfo implements Serializable {
  private String column;
  @Builder.Default private DataType dataType = DataType.STRING;
  private Integer ordinalPosition;
  @Builder.Default private ExcelSpecificDataType excelDataType = ExcelSpecificDataType.STRING;
  @Builder.Default private UUID columnId = null;
  @Builder.Default private String bindingName = "";
}
