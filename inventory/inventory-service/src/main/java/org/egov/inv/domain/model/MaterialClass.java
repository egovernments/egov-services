package org.egov.inv.domain.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MaterialClass {

    HIGHUSAGE("HighUsage"), MEDIUMUSAGE("MediumUsage"), LOWUSAGE("LowUsage");

    private String value;

    MaterialClass(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static MaterialClass fromValue(String text) {
        for (MaterialClass b : MaterialClass.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
