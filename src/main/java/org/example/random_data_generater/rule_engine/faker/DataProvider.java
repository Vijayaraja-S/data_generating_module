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
    private final Faker faker;
    public   String getData(TypeData dataType, DataGeneratorBean dataGeneratorBean){
        switch (dataType){
            case DATE:
                return ruleChecker.checkWithRule(dataGeneratorBean,faker);

        }
        return null;
    }
}
