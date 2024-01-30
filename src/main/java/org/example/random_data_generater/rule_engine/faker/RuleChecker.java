package org.example.random_data_generater.rule_engine.faker;

import com.github.javafaker.Faker;
import java.text.SimpleDateFormat;
import org.example.random_data_generater.bean.ColumnDataInfo;
import org.example.random_data_generater.bean.DataGeneratorBean;
import org.example.random_data_generater.bean.date_time.GlobalDateFormat;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class RuleChecker {
    public String checkWithRule(DataGeneratorBean dataGeneratorBean,Faker faker){
        ColumnDataInfo columnInfo = dataGeneratorBean.getColumnInfo();
        switch (dataGeneratorBean.getTypeData()){
            case DATE:
                return checkDateRule(columnInfo.getDataFormat(),columnInfo.getFromDate(),columnInfo.getToDate(),faker);
        }
        return null;
    }

    private String checkDateRule(GlobalDateFormat dataFormat,
                               Date fromDate,
                               Date toDate, Faker faker) {
        Date date = faker.date().between(fromDate, toDate);
        SimpleDateFormat sdf = new SimpleDateFormat(dataFormat.getValue());
        return sdf.format(date);
    }

}
