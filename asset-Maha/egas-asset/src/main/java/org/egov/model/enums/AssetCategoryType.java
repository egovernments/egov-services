package org.egov.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AssetCategoryType {
	
    LAND("LAND"), MOVABLE("MOVABLE"), IMMOVABLE("IMMOVABLE");

    private String value;

    AssetCategoryType(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static AssetCategoryType fromValue(String text) {
      for (AssetCategoryType b : AssetCategoryType.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

