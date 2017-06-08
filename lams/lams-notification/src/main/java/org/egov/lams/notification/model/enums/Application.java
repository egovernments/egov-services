package org.egov.lams.notification.model.enums;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Application {
    CREATE("CREATE"),
    
    RENEWAL("RENEWAL"),
    
    EVICTION("EVICTION"),
    
    CANCEL("CANCEL");

    private String value;

    Application(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static Application fromValue(String text) {
      for (Application b : Application.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

