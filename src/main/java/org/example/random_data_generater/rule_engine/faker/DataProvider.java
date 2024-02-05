package org.example.random_data_generater.rule_engine.faker;

import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import org.example.random_data_generater.bean.DataGeneratorBean;
import org.example.random_data_generater.bean.enums.TypeData;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
@AllArgsConstructor
public class DataProvider {
    private final  RuleChecker ruleChecker;

    public   String getData(TypeData dataType,DataGeneratorBean dataGeneratorBean, Faker faker) throws ParseException {
        return switch (dataType) {
            case CREDIT_CARD, DATE,GENDER-> ruleChecker.checkWithRule(dataGeneratorBean,faker);
            case ADDRESS -> faker.address().fullAddress();
            case CITY -> faker.address().city();
            case ZIP_CODE -> faker.address().zipCode();
            case STREET_NAME -> faker.address().streetName();
            case FIRST_NAME ->faker.name().firstName();
            case FULL_NAME ->faker.name().fullName();
            case LAST_NAME -> faker.name().lastName();
            case EMAIL -> faker.internet().emailAddress();
            case PHONE_NUMBER -> faker.phoneNumber().cellPhone();
            case SHA_256_HASHCODE ->faker.crypto().sha256();
            case PASSWORD_HASHCODE -> faker.crypto().md5();
            default -> null;
        };
    }
}
