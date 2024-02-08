package com.p3.poc.bean.enums;

public enum Timezone {
    IST("Asia/Kolkata"),
    PST("America/Los_Angeles"),
    ECT("Europe/Paris");

    private final String value;

    Timezone( String value) {
        this.value = value;
    }
    public String getValue() {
        return this.value;
    }


}
