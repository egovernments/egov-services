package org.egov.works.estimate.web.contract;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Gets or Sets Beneficiary
 */
public enum Beneficiary {
  
  SC("SC"),
  
  ST("ST"),
  
  BC("BC"),
  
  MINORITY("MINORITY"),
  
  WOMEN_CHILDREN_WELFARE("WOMEN_CHILDREN_WELFARE"),
  
  GENERAL("GENERAL");

  private String value;

  Beneficiary(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static Beneficiary fromValue(String text) {
    for (Beneficiary b : Beneficiary.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

