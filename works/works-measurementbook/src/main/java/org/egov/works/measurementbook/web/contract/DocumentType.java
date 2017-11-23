package org.egov.works.measurementbook.web.contract;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

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

