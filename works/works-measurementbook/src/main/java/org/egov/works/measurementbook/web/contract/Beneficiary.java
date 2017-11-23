package org.egov.works.measurementbook.web.contract;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.validation.Valid;
import javax.validation.constraints.*;

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

