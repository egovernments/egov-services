package org.egov.works.measurementbook.web.contract;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

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

