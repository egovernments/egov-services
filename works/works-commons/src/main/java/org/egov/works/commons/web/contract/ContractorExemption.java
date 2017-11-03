package org.egov.works.commons.web.contract;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Gets or Sets ContractorExemption
 */
public enum ContractorExemption {
  
  INCOME_TAX_("INCOME_TAX,"),
  
  EARNEST_MONEY_DEPOSIT("EARNEST_MONEY_DEPOSIT"),
  
  VAT("VAT");

  private String value;

  ContractorExemption(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static ContractorExemption fromValue(String text) {
    for (ContractorExemption b : ContractorExemption.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

