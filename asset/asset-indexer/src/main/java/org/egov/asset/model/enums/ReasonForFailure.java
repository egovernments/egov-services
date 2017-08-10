package org.egov.asset.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ReasonForFailure {


    DEPRECIATION_RATE_NOT_FOUND("DEPRECIATION_RATE_NOT_FOUND");

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
