package org.example.random_data_generater.bean.enums;

import java.io.Serializable;

public enum GlobalDateTimeFormat implements Serializable {
    DATETIME_FORMAT_1("yyyy-MM-dd hh:mm:ss.ssssss"),
    DATETIME_FORMAT_2("yyyy/MM/dd hh:mm:ss.ssssss"),
    DATETIME_FORMAT_3("MM/dd/yyyy hh:mm:ss"),
    DATETIME_FORMAT_4("dd/MM/yyyy hh:mm:ss"),
    DATETIME_FORMAT_5("dd-MM-yyyy hh:mm:ss"),
    DATETIME_FORMAT_6("MM-dd-yyyy hh:mm:ss"),
    DATETIME_FORMAT_7("yyyy/MM/dd hh:mm:ss"),
    DATETIME_FORMAT_8("yyyy-MM-dd hh:mm:ss"),
    DATETIME_FORMAT_9("yyyyMMddhhmmss.sss"),
    //AM PM
    DATETIME_FORMAT_10("yyyy-MM-dd hh:mm:ss.ssssss a"),//2020-12-10 12:12:00 AM
    DATETIME_FORMAT_11("yyyy/MM/dd hh:mm:ss.ssssss a"),
    DATETIME_FORMAT_12("MM/dd/yyyy hh:mm:ss a"),
    DATETIME_FORMAT_13("dd/MM/yyyy hh:mm:ss a"),
    DATETIME_FORMAT_14("dd-MM-yyyy hh:mm:ss a"),
    DATETIME_FORMAT_15("MM-dd-yyyy hh:mm:ss a"),
    DATETIME_FORMAT_16("yyyy/MM/dd hh:mm:ss a"),
    DATETIME_FORMAT_17("yyyy-MM-dd hh:mm:ss a"),
    DATETIME_FORMAT_18("yyyyMMddhhmmss.sss a"),
    DATETIME_FORMAT_28("yyyyMMddhhmmss.sssa"),
    //AM PM
    DATETIME_FORMAT_19("yyyy-MM-dd hh:mm:ss.ssssssa"),//2020-12-10 08:12:00AM
    DATETIME_FORMAT_20("yyyy/MM/dd hh:mm:ss.ssssssa"),
    DATETIME_FORMAT_21("MM/dd/yyyy hh:mm:ssa"),
    DATETIME_FORMAT_22("dd/MM/yyyy hh:mm:ssa"),
    DATETIME_FORMAT_23("dd-MM-yyyy hh:mm:ssa"),
    DATETIME_FORMAT_24("MM-dd-yyyy hh:mm:ssa"),
    DATETIME_FORMAT_25("yyyy/MM/dd hh:mm:ssa"),
    DATETIME_FORMAT_26("yyyy-MM-dd hh:mm:ssa"),
    DATETIME_FORMAT_27("yyyyMMddhhmmss.sssa");

    private String value;

    GlobalDateTimeFormat(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


    public static GlobalDateTimeFormat getKey(String type) {
        for (GlobalDateTimeFormat format : GlobalDateTimeFormat.values()) {
            if (type.equalsIgnoreCase(format.getValue()))
                return format;
        }
        return null;
    }

}

