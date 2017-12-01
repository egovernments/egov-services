package org.egov.works.workorder.web.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets AdvanceRequisitionType
 */
public enum AdvanceRequisitionType {

    CONTRACTOR("CONTRACTOR"),

    SUPPLIER("SUPPLIER"),

    SALARY("SALARY");

    private String value;

    AdvanceRequisitionType(String value) {
        this.value = value;
    }

    @JsonCreator
    public static AdvanceRequisitionType fromValue(String text) {
        for (AdvanceRequisitionType b : AdvanceRequisitionType.values()) {
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

