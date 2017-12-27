package org.egov.works.workorder.web.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets WorkOrderStatus
 */
public enum NoticeStatus {
  
  CREATED("CREATED"),
  
  REJECTED("REJECTED"),
  
  APPROVED("APPROVED"),
  
  CANCELLED("CANCELLED"),
  
  RESUBMITTED("RESUBMITTED");
  
  private String value;

  NoticeStatus(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static NoticeStatus fromValue(String text) {
    for (NoticeStatus b : NoticeStatus.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

