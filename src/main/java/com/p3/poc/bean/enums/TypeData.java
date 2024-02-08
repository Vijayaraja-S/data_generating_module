package com.p3.poc.bean.enums;

import com.p3.export.specifics.DataType;

public enum TypeData {
     ADDRESS(DataType.STRING),
     BLOB(DataType.BLOB),
     CREDIT_CARD(DataType.NUMBER), CITY(DataType.STRING),
     DATE(DataType.DATE),DECIMAL(DataType.DECIMAL),
     EMAIL(DataType.STRING),
     FIRST_NAME(DataType.STRING),FULL_NAME(DataType.STRING),
     GENDER(DataType.STRING),
     LAST_NAME(DataType.STRING),
     PHONE_NUMBER(DataType.NUMBER), PASSWORD(DataType.STRING), PARAGRAPH(DataType.STRING),
     ROW_NUMBER(DataType.NUMBER),
     STREET_NAME(DataType.STRING),SHA_256_HASHCODE(DataType.STRING),SENTENCE(DataType.STRING),
     TIMESTAMP_TIMEZONE(DataType.DATETIME),TIMESTAMP( DataType.DATETIME),
     WORDS(DataType.STRING),
     ZIP_CODE(DataType.NUMBER),
     ;
     private DataType dataType;
     TypeData(final DataType dataType) {
          this.dataType=dataType;
     }
     public DataType getDataType() {
          return this.dataType;
     }
}
