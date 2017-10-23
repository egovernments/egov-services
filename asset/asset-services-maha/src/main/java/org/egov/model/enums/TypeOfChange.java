package org.egov.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TypeOfChange {
    INCREASED("INCREASED"),
    
    DECREASED("DECREASED");

    private String value;

    TypeOfChange(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TypeOfChange fromValue(String text) {
      for (TypeOfChange b : TypeOfChange.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }