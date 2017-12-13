package org.egov.works.masters.web.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets ProjectCodeStatus
 */
public enum ProjectCodeStatus {
  
  CREATED("CREATED"),
  
  CLOSED("CLOSED");

  private String value;

  ProjectCodeStatus(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static ProjectCodeStatus fromValue(String text) {
    for (ProjectCodeStatus b : ProjectCodeStatus.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

