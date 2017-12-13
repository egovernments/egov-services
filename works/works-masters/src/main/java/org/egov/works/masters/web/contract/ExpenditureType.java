package org.egov.works.masters.web.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets ExpenditureType
 */
public enum ExpenditureType {
  
  CAPITAL("CAPITAL"),
  
  REVENUE("REVENUE"),
  
  OTHERS("OTHERS");

  private String value;

  ExpenditureType(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static ExpenditureType fromValue(String text) {
    for (ExpenditureType b : ExpenditureType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

