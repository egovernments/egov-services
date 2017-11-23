package org.egov.works.measurementbook.web.contract;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Gets or Sets WorkProgress
 */
public enum WorkProgress {
  
  BEFORE("BEFORE"),
  
  DURING("DURING"),
  
  AFTER("AFTER");

  private String value;

  WorkProgress(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static WorkProgress fromValue(String text) {
    for (WorkProgress b : WorkProgress.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

