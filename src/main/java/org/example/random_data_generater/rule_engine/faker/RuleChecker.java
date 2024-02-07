package org.example.random_data_generater.rule_engine.faker;

import com.github.javafaker.CreditCardType;
import com.github.javafaker.Faker;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.example.random_data_generater.bean.ColumnRules;
import org.example.random_data_generater.bean.columnDetails;
import org.example.random_data_generater.bean.enums.GlobalDateFormat;
import org.example.random_data_generater.bean.enums.Timezone;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Date;

@Component
public class RuleChecker {
    public String checkWithRule(columnDetails dataGeneratorBean, Faker faker) throws ParseException {
        ColumnRules columnInfo = dataGeneratorBean.getColumnRules();
        switch (dataGeneratorBean.getTypeData()){
            case DATE:
                return checkDateRule(columnInfo.getDataFormat(),columnInfo.getFromDate(),columnInfo.getToDate(),faker);
            case CREDIT_CARD:
                return checkCreditCardRule(columnInfo.getCreditCardType(),faker);
            case GENDER:
                return generateGenderData(faker);
            case TIMESTAMP_TIMEZONE:
                return generateTimestampWithZone(columnInfo.getFromTimestampDate(),columnInfo.getToTimestampDate(),columnInfo.getTimezone());
            case TIMESTAMP:
                return generateTimestamp(columnInfo.getFromTimestampDate(),columnInfo.getToTimestampDate()).toString();
        }
        return null;
    }

    private String generateTimestampWithZone(String fromTimestampDate,
                                             String toTimestampDate,
                                             Timezone timezone){
        Timestamp timestamp = generateTimestamp(fromTimestampDate, toTimestampDate);
        ZoneId targetZoneId = ZoneId.of(timezone.getValue());
        return timestamp.toInstant().atZone(targetZoneId).toString();
    }

    private Timestamp generateTimestamp(String fromTimestampDate, String toTimestampDate) {
        long offset = Timestamp.valueOf(String.format("%s 00:00:00",fromTimestampDate)).getTime();
        long end = Timestamp.valueOf(String.format("%s 00:00:00",toTimestampDate)).getTime();
        long diff = end - offset + 1;
        return new Timestamp(offset + (long)(Math.random() * diff));
    }

    private String generateGenderData(Faker faker) {
        int random = faker.number().randomDigit();
        String gender;
        if (random / 2 == 0) {
            gender = "MALE";
        } else {
            gender = "FEMALE";
        }
        return gender;
    }

    private String checkCreditCardRule(String creditCardType, Faker faker) {
        return faker.finance().creditCard(CreditCardType.valueOf(creditCardType));
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
