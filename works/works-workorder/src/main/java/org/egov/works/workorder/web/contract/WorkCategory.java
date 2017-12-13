package org.egov.works.workorder.web.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets WorkCategory
 */
public enum WorkCategory {

    NON_SLUM("NON_SLUM"),

    NOTIFIED_SLUM("NOTIFIED_SLUM"),

    NON_NOTIFIED_SLUM("NON_NOTIFIED_SLUM");

    private String value;

    WorkCategory(String value) {
        this.value = value;
    }

    @JsonCreator
    public static WorkCategory fromValue(String text) {
        for (WorkCategory b : WorkCategory.values()) {
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

