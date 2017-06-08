package org.egov.asset.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TypeOfChangeEnum {
    INCREASED("INCREASED"),
    
    DECREASED("DECREASED");

    private String value;

    TypeOfChangeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TypeOfChangeEnum fromValue(String text) {
      for (TypeOfChangeEnum b : TypeOfChangeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }