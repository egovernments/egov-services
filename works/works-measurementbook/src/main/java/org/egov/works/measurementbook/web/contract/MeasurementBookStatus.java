package org.egov.works.measurementbook.web.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets MeasurementBookStatus
 */
public enum MeasurementBookStatus {
  
  NEW("NEW"),
  
  CREATED("CREATED"),
  
  CHECKED("CHECKED"),
  
  APPROVED("APPROVED"),
  
  REJECTED("REJECTED"),
  
  RESUBMITTED("RESUBMITTED"),
  
  CANCELLED("CANCELLED"), 
  SAVED("SAVED");

  private String value;

  MeasurementBookStatus(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static MeasurementBookStatus fromValue(String text) {
    for (MeasurementBookStatus b : MeasurementBookStatus.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

