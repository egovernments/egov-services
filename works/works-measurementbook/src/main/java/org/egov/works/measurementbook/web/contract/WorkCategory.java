package org.egov.works.measurementbook.web.contract;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Gets or Sets WorkCategory
 */
public enum WorkCategory {
  
  NON_SLUM("NON_SLUM"),
  
  NOTIFIED_SLUM("NOTIFIED_SLUM"),
  
  NON_NOTIFIED_SLUM("NON_NOTIFIED_SLUM");

  private String value;

  WorkCategory(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static WorkCategory fromValue(String text) {
    for (WorkCategory b : WorkCategory.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

