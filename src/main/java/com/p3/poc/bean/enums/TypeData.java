package com.p3.poc.bean.enums;

public enum TypeData {
     ADDRESS("STRING"),
     BLOB("BLOB"),
     CREDIT_CARD("NUMBER"), CITY("STRING"),
     DATE("DATE"),DECIMAL("DECIMAL"),
     EMAIL("STRING"),
     FIRST_NAME("STRING"),FULL_NAME("STRING"),
     GENDER("STRING"),
     LAST_NAME("STRING"),
     PHONE_NUMBER("NUMBER"), PASSWORD("STRING"), PARAGRAPH("STRING"),
     ROW_NUMBER("NUMBER"),
     STREET_NAME("STRING"),SHA_256_HASHCODE("STRING"),SENTENCE("STRING"),
     TIMESTAMP_TIMEZONE("DATETIME"),TIMESTAMP("DATETIME"),
     WORDS("STRING"),
     ZIP_CODE("NUMBER"),
     ;
     private String dataType;
     TypeData(final String dataType) {
          this.dataType=dataType;
     }
     public String getDataType() {
          return this.dataType;
     }
}
