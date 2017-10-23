package org.egov.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ModeOfAcquisitionEnum {
    ACQUIRED("ACQUIRED"),
    
    CONSTRUCTION("CONSTRUCTION"),
    
    PURCHASE("PURCHASE"),
    
    TENDER("TENDER"),
    
    DONATION("DONATION");

    private String value;

    ModeOfAcquisitionEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ModeOfAcquisitionEnum fromValue(String text) {
      for (ModeOfAcquisitionEnum b : ModeOfAcquisitionEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }