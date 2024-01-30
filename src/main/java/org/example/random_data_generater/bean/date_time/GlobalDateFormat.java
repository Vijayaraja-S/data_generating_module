package org.example.random_data_generater.bean.date_time;

import java.io.Serializable;

public enum GlobalDateFormat implements Serializable {

    DATE_FORMAT_1("dd/MM/yyyy"),
    DATE_FORMAT_2("dd/yyyy/MM"),
    DATE_FORMAT_3("MM/dd/yyyy"),
    DATE_FORMAT_4("MM/yyyy/dd"),
    DATE_FORMAT_5("yyyy/dd/MM"),
    DATE_FORMAT_6("yyyy/MM/dd"),
    DATE_FORMAT_7("dd-MM-yyyy"),
    DATE_FORMAT_8("dd-yyyy-MM"),
    DATE_FORMAT_9("MM-dd-yyyy"),
    DATE_FORMAT_10("MM-yyyy-dd"),
    DATE_FORMAT_11("yyyy-dd-MM"),
    DATE_FORMAT_12("yyyy-MM-dd"),
    DATE_FORMAT_13("ddMMyyyy"),
    DATE_FORMAT_14("ddyyyyMM"),
    DATE_FORMAT_15("MMddyyyy"),
    DATE_FORMAT_16("MMyyyydd"),
    DATE_FORMAT_17("yyyyddMM"),
    DATE_FORMAT_18("yyyyMMdd"),

    DATE_FORMAT_19("dd/MMM/yyyy"),
    DATE_FORMAT_20("dd/yyyy/MMM"),
    DATE_FORMAT_21("MMM/dd/yyyy"),
    DATE_FORMAT_22("MMM/yyyy/dd"),
    DATE_FORMAT_23("yyyy/dd/MMM"),
    DATE_FORMAT_24("yyyy/MMM/dd"),
    DATE_FORMAT_25("dd-MMM-yyyy"),
    DATE_FORMAT_26("dd-yyyy-MMM"),
    DATE_FORMAT_27("MMM-dd-yyyy"),
    DATE_FORMAT_28("MMM-yyyy-dd"),
    DATE_FORMAT_29("yyyy-dd-MMM"),
    DATE_FORMAT_30("yyyy-MMM-dd"),
    DATE_FORMAT_31("ddMMMyyyy"),
    DATE_FORMAT_32("ddyyyyMMM"),
    DATE_FORMAT_33("MMMddyyyy"),
    DATE_FORMAT_34("MMMyyyydd"),
    DATE_FORMAT_35("yyyyddMMM"),
    DATE_FORMAT_36("yyyyMMMdd"),

    DATE_FORMAT_37("yy-MM-dd"),
    DATE_FORMAT_38("MM-dd-yy"),
    DATE_FORMAT_39("dd-MM-yy"),
    DATE_FORMAT_40("MMM-dd-yy"),
    DATE_FORMAT_41("dd-MMM-yy"),
    DATE_FORMAT_42("yy-MMM-dd"),
    DATE_FORMAT_43("dd/MM/yy"),
    DATE_FORMAT_44("yy/MM/dd"),
    DATE_FORMAT_45("dd/MMM/yy"),
    DATE_FORMAT_46("MMM/dd/yy"),
    DATE_FORMAT_47("yy/MMM/dd"),
    DATE_FORMAT_48("MMddyy"),
    DATE_FORMAT_49("ddMMyy"),
    DATE_FORMAT_50("MMMddyy"),
    DATE_FORMAT_51("MM.dd.yy"),
    DATE_FORMAT_52("dd.MM.yy"),
    DATE_FORMAT_53("MMM.dd.yy"),
    DATE_FORMAT_54("dd.MMM.yyyy"),
    DATE_FORMAT_55("dd.yyyy.MMM"),
    DATE_FORMAT_56("MMM.dd.yyyy"),
    DATE_FORMAT_57("MMM.yyyy.dd"),
    DATE_FORMAT_58("yyyy.dd.MMM"),
    DATE_FORMAT_59("yyyy.MMM.dd"),

    DATE_FORMAT_60("dd.MM.yyyy"),
    DATE_FORMAT_61("dd.yyyy.MM"),
    DATE_FORMAT_62("MM.dd.yyyy"),
    DATE_FORMAT_63("MM.yyyy.dd"),
    DATE_FORMAT_64("yyyy.dd.MM"),
    DATE_FORMAT_65("yyyy.MM.dd");




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
