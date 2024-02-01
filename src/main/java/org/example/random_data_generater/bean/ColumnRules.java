package org.example.random_data_generater.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.random_data_generater.bean.enums.CreditCardType;
import org.example.random_data_generater.bean.enums.GlobalDateFormat;

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
    private CreditCardType creditCardType;
//    @Builder.Default private Boolean upperCase=false;
//    private GlobalTimeFormat timeFormat;
//    private GlobalDateTimeFormat dateTimeFormat;
//    private  int lowRangeMoney;
//    private  int highRangeMoney;
//    private int[][] avatarSize;
//    private Character starts_with;
//    private Character ends_with;
}
