package org.egov.demand.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum InstallmentType {
    MONTH("MONTH"),
    
    QUARTER("QUARTER"),
    
    HALFYEAR("HALFYEAR"),
    
    ANNUAL("ANNUAL");

    private String value;

    InstallmentType(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static InstallmentType fromValue(String text) {
      for (InstallmentType b : InstallmentType.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
