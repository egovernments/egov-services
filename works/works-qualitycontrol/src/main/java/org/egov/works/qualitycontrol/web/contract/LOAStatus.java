package org.egov.works.qualitycontrol.web.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets LOAStatus
 */
public enum LOAStatus {

    CREATED("CREATED"),

    REJECTED("REJECTED"),

    APPROVED("APPROVED"),

    CANCELLED("CANCELLED"),

    RESUBMITTED("RESUBMITTED"),

    CHECKED("CHECKED");

    private String value;

    LOAStatus(String value) {
        this.value = value;
    }

    @JsonCreator
    public static LOAStatus fromValue(String text) {
        for (LOAStatus b : LOAStatus.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }
}

