package org.egov.inv.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets AdvanceRequisitionStatus
 */
public enum AdvanceRequisitionStatus {

    CREATED("CREATED"),

    REJECTED("REJECTED"),

    CANCELLED("CANCELLED"),

    APPROVED("APPROVED"),

    RESUBMITTED("RESUBMITTED"),

    CHECKED("CHECKED");

    private String value;

    AdvanceRequisitionStatus(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static AdvanceRequisitionStatus fromValue(String text) {
        for (AdvanceRequisitionStatus b : AdvanceRequisitionStatus.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}

