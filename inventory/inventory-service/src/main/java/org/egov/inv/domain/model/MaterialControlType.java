package org.egov.inv.domain.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MaterialControlType {

    LOTCONTROL("LOTControl"), SHELFLIFECONTROl("Shelf_life_Control");

    private String value;

    MaterialControlType(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static MaterialControlType fromValue(String text) {
        for (MaterialControlType b : MaterialControlType.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
