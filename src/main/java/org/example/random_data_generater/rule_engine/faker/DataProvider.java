package org.example.random_data_generater.rule_engine.faker;

import com.github.javafaker.Faker;

public class DataProvider {
    public static String getData(String dataType, Faker faker){
        switch (dataType.toUpperCase()){
            case "CRYPTO_HASH_VALUE":
                return faker.crypto().md5();
            case "CREDIT_CARD":
                return faker.business().creditCardNumber();
            case "CREDIT_CARD_TYPE":
                return faker.business().creditCardType();
            case "CREDIT_CARD_EXPIRY":
                return faker.business().creditCardExpiry();
            case "COUNTRY_CODE":
                return  faker.country().countryCode3();
            case "CURRENCY_CODE":
                return  faker.country().currencyCode();
            case "FIRSTNAME":
                return faker.name().firstName();
            case "FULL_NAME":
                return faker.name().fullName();
            case "LASTNAME":
                return faker.name().lastName();
            case "STREET_NAME":
                return faker.address().streetName();
            case "STREET_ADDRESS":
                return faker.address().streetAddress();
        }
        return null;
    }
}
