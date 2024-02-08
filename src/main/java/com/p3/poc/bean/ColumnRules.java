package com.p3.poc.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.p3.poc.bean.enums.GlobalDateFormat;
import com.p3.poc.bean.enums.Timezone;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ColumnRules {
    @Builder.Default private int reoccurrence = 0;
    @Builder.Default private int blank = 0;
    private GlobalDateFormat dataFormat;
    private String fromDate;
    private String toDate;
    private String creditCardType;
    @Builder.Default private String fromTimestampDate = "2012-01-01";
    @Builder.Default private String toTimestampDate = "2015-01-01";
    @Builder.Default private Timezone timezone = Timezone.IST;
//    @Builder.Default private Boolean upperCase=false;
//    private GlobalTimeFormat timeFormat;
//    private GlobalDateTimeFormat dateTimeFormat;
//    private  int lowRangeMoney;
//    private  int highRangeMoney;
//    private int[][] avatarSize;
//    private Character starts_with;
//    private Character ends_with;
}
