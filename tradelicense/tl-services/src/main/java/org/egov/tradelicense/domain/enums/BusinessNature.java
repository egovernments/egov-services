package org.egov.tradelicense.domain.enums;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Gets or Sets BusinessNature
 */
public enum BusinessNature {
  
  PERMENANT("PERMENANT"),
  
  TEMPORARY("TEMPORARY");

  private String value;

  BusinessNature(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static BusinessNature fromValue(String text) {
    for (BusinessNature b : BusinessNature.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

