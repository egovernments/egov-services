package org.egov.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ReasonForFailure {


    DEPRECIATION_RATE_NOT_FOUND("DEPRECIATION_RATE_NOT_FOUND"),
	
	ASSET_IS_FULLY_DEPRECIATED_TO_MINIMUN_VALUE("ASSET_IS_FULLY_DEPRECIATED_TO_MINIMUM_VALUE"),
	
	CAPITALISED_VALUE_IS_ALREADY_MINIMUM("CAPITALISED_VALUE_IS_ALREADY_MINIMUM");

    private String value;

    ReasonForFailure(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ReasonForFailure fromValue(String text) {
      for (ReasonForFailure b : ReasonForFailure.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
}
