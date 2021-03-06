package org.egov.works.workorder.web.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets MilestoneStatus
 */
public enum MilestoneStatus {

    APPROVED("APPROVED"),

    CANCELLED("CANCELLED");

    private String value;

    MilestoneStatus(String value) {
        this.value = value;
    }

    @JsonCreator
    public static MilestoneStatus fromValue(String text) {
        for (MilestoneStatus b : MilestoneStatus.values()) {
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

