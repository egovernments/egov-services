package org.egov.works.qualitycontrol.web.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets WorkOrderStatus
 */
public enum WorkOrderStatus {
  
  CREATED("CREATED"),
  
  REJECTED("REJECTED"),
  
  APPROVED("APPROVED"),
  
  CANCELLED("CANCELLED"),
  
  RESUBMITTED("RESUBMITTED"),
  
  CHECKED("CHECKED");

  private String value;

  WorkOrderStatus(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static WorkOrderStatus fromValue(String text) {
    for (WorkOrderStatus b : WorkOrderStatus.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

