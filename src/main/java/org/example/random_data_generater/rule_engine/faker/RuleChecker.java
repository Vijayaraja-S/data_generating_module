package org.example.random_data_generater.rule_engine.faker;

import com.github.javafaker.Faker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.example.random_data_generater.bean.ColumnRules;
import org.example.random_data_generater.bean.DataGeneratorBean;
import org.example.random_data_generater.bean.enums.GlobalDateFormat;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class RuleChecker {
    public String checkWithRule(DataGeneratorBean dataGeneratorBean,Faker faker) throws ParseException {
        ColumnRules columnInfo = dataGeneratorBean.getColumnRules();
        switch (dataGeneratorBean.getTypeData()){
            case DATE:
                return checkDateRule(columnInfo.getDataFormat(),columnInfo.getFromDate(),columnInfo.getToDate(),faker);
        }
        return null;
    }

    private String checkDateRule(GlobalDateFormat dataFormat,
                               String fromDate,
                               String toDate, Faker faker) throws ParseException {
        Date start = dateChanger(fromDate);
        Date end = dateChanger(toDate);
        Date date = faker.date().between(start, end);
        SimpleDateFormat sdf = new SimpleDateFormat(dataFormat.getValue());
        return sdf.format(date);
    }

    private Date dateChanger(String date) throws ParseException {
        DateFormat df = new SimpleDateFormat("mm/dd/yyyy");
       return df.parse(date);
    }

}
