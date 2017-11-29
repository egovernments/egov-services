package org.egov.works.estimate.web.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets ContractorBillStatus
 */
public enum ContractorBillStatus {
  
  NEW("NEW"),
  
  CREATED("CREATED"),
  
  CHECKED("CHECKED"),
  
  APPROVED("APPROVED"),
  
  REJECTED("REJECTED"),
  
  RESUBMITTED("RESUBMITTED"),
  
  CANCELLED("CANCELLED");

  private String value;

  ContractorBillStatus(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static ContractorBillStatus fromValue(String text) {
    for (ContractorBillStatus b : ContractorBillStatus.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

