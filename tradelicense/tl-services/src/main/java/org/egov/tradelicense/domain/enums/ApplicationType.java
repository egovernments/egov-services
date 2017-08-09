package org.egov.tradelicense.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets ApplicationType
 */
public enum ApplicationType {
  
  NEW("NEW"),
  
  RENEW("RENEW");

  private String value;

  ApplicationType(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static ApplicationType fromValue(String text) {
    for (ApplicationType b : ApplicationType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

