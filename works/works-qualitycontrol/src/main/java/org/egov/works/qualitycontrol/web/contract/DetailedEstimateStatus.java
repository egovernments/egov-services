package org.egov.works.qualitycontrol.web.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets DetailedEstimateStatus
 */
public enum DetailedEstimateStatus {
  
  NEW("NEW"),
  
  CREATED("CREATED"),
  
  REJECTED("REJECTED"),
  
  RESUBMITTED("RESUBMITTED"),
  
  CANCELLED("CANCELLED"),
  
  APPROVED("APPROVED"),
  
  CHECKED("CHECKED"),
  
  TECHNICAL_SANCTIONED("TECHNICAL_SANCTIONED");

  private String value;

  DetailedEstimateStatus(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static DetailedEstimateStatus fromValue(String text) {
    for (DetailedEstimateStatus b : DetailedEstimateStatus.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

