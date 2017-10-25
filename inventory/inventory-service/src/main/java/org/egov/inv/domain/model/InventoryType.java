package org.egov.inv.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


public enum InventoryType {


    ASSET("Asset"), CONSUMABLE("Consumable");

    private String value;

    InventoryType(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static InventoryType fromValue(String text) {
        for (InventoryType b : InventoryType.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
