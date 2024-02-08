package com.p3.poc.faker;

import com.github.javafaker.Faker;
import com.p3.poc.bean.ColumnEntity;
import com.p3.poc.bean.enums.TypeData;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
@AllArgsConstructor
public class DataProvider {
    private final  RuleChecker ruleChecker;

    public   String getData(TypeData dataType, ColumnEntity dataGeneratorBean, Faker faker) throws ParseException {

        return switch (dataType) {
            case CREDIT_CARD, DATE,GENDER,TIMESTAMP,TIMESTAMP_TIMEZONE-> ruleChecker.checkWithRule(dataGeneratorBean,faker);
            case ADDRESS -> faker.address().fullAddress()+ RandomStringUtils.randomAlphabetic(3);
            case BLOB ->faker.avatar().image();
            case CITY -> faker.address().city()+ RandomStringUtils.randomAlphabetic(3);
            case DECIMAL ->String.valueOf(faker.number().randomDouble(4,100,200));
            case EMAIL -> faker.internet().emailAddress();
            case FIRST_NAME ->faker.name().firstName() + RandomStringUtils.randomAlphabetic(3);
            case FULL_NAME ->faker.name().fullName()+ RandomStringUtils.randomAlphabetic(3);
            case LAST_NAME -> faker.name().lastName()+ RandomStringUtils.randomAlphabetic(3);
            case PHONE_NUMBER -> faker.phoneNumber().cellPhone();
            case PASSWORD -> faker.crypto().md5();
            case SHA_256_HASHCODE ->faker.crypto().sha256();
            case STREET_NAME -> faker.address().streetName();
            case ZIP_CODE -> faker.address().zipCode();
            case SENTENCE -> faker.lorem().sentence();
            default -> null;
        };
    }
}
