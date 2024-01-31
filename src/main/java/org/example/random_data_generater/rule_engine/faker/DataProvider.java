package org.example.random_data_generater.rule_engine.faker;

import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import org.example.random_data_generater.bean.DataGeneratorBean;
import org.example.random_data_generater.bean.TypeData;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataProvider {
    private final  RuleChecker ruleChecker;

    public   String getData(TypeData dataType,DataGeneratorBean dataGeneratorBean, Faker faker){
        return switch (dataType) {
            case ADDRESS -> faker.address().fullAddress();
            case CITY -> faker.address().city();
            case ZIP_CODE -> faker.address().zipCode();
            case STREET_NAME -> faker.address().streetName();
            default -> null;
        };
    }
}
