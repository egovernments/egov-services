package org.egov.works.estimate.web.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets AbstractEstimateStatus
 */
public enum AbstractEstimateStatus {
  
  NEW("NEW"),
  
  CREATED("CREATED"),
  
  FINANCIAL_SANCTIONED("FINANCIAL_SANCTIONED"),
  
  ADMIN_SANCTIONED("ADMIN_SANCTIONED"),
  
  APPROVED("APPROVED"),
  
  REJECTED("REJECTED"),
  
  CANCELLED("CANCELLED"),
  
  RESUBMITTED("RESUBMITTED"),
  
  CHECKED("CHECKED");

  private String value;

  AbstractEstimateStatus(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static AbstractEstimateStatus fromValue(String text) {
    for (AbstractEstimateStatus b : AbstractEstimateStatus.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

