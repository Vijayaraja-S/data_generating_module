package org.example.random_data_generater.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.random_data_generater.bean.date_time.GlobalDateFormat;
import org.example.random_data_generater.bean.date_time.GlobalDateTimeFormat;
import org.example.random_data_generater.bean.date_time.GlobalTimeFormat;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ColumnDataInfo {
    @Builder.Default private Boolean upperCase=false;
    private GlobalTimeFormat timeFormat;
    private GlobalDateFormat dataFormat;
    private GlobalDateTimeFormat dateTimeFormat;
    private Date fromDate;
    private Date toDate;
    private  int lowRangeMoney;
    private  int highRangeMoney;
    private int[][] avatarSize;
    private Character starts_with;
    private Character ends_with;
}
