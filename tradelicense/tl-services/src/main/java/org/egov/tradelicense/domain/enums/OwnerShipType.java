package org.egov.tradelicense.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets OwnerShipType
 */
public enum OwnerShipType {
  
  STATE_GOVERNMENT("STATE_GOVERNMENT"),
  
  RENTED("RENTED");

  private String value;

  OwnerShipType(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static OwnerShipType fromValue(String text) {
    for (OwnerShipType b : OwnerShipType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

