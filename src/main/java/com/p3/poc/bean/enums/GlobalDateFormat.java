package com.p3.poc.bean.enums;

import java.io.Serializable;

public enum GlobalDateFormat implements Serializable {

    DD_MM_YYYY ("dd/MM/yyyy"),
    DD_YYYY_MM("dd/yyyy/MM"),
    MM_DD_YYYY("MM/dd/yyyy"),
    MM_YYYY_DD("MM/yyyy/dd"),
    YYYY_DD_MM("yyyy/dd/MM"),
    YYYY_MM_DD("yyyy/MM/dd");

    private String value;

    GlobalDateFormat(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static GlobalDateFormat getKey(String type) {
        for (GlobalDateFormat format : GlobalDateFormat.values()) {
            if (type.equalsIgnoreCase(format.getValue()))
                return format;
        }
        return null;
    }

}
