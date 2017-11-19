package org.egov.works.qualitycontrol.web.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets DocumentType
 */
public enum DocumentType {
  
  ESTIMATE_PHOTOGRAPHS("ESTIMATE_PHOTOGRAPHS"),
  
  DOCUMENTS("DOCUMENTS"),
  
  NOTICE("NOTICE");

  private String value;

  DocumentType(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static DocumentType fromValue(String text) {
    for (DocumentType b : DocumentType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

