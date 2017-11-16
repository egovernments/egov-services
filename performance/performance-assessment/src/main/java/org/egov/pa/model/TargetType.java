package org.egov.pa.model;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TargetType {
	TEXT("TEXT"), VALUE("VALUE"), OBJECTIVE("OBJECTIVE");

    private String value;

    TargetType(final String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return StringUtils.capitalize(name());
    }

    @JsonCreator
    public static TargetType fromValue(final String passedValue) {
        for (final TargetType obj : TargetType.values())
            if (String.valueOf(obj.value).equals(passedValue.toUpperCase()))
                return obj;
        return null;
    }
}
