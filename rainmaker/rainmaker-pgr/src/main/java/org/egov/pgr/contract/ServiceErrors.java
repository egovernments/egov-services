package org.egov.pgr.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets ServiceErrors
 */

public enum ServiceErrors {
  
  SERVICENOTFOUND("ServiceNotFound"),
  
  INVALIDSEARCHPARAMS("InvalidSearchParams"),
  
  TENANTNOTFOUND("TenantNotFound"),
  
  SEARCHCRITERIATOOBROAD("SearchCriteriaTooBroad");

  private String value;

  ServiceErrors(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static ServiceErrors fromValue(String text) {
    for (ServiceErrors b : ServiceErrors.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

