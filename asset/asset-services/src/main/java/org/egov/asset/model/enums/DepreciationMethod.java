
package org.egov.asset.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DepreciationMethod {

    STRAIGHT_LINE_METHOD("STRAIGHT_LINE_METHOD"), WRITTEN_DOWN_METHOD("WRITTEN_DOWN_METHOD");

    private String value;

    DepreciationMethod(final String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static DepreciationMethod fromValue(final String text) {
        for (final DepreciationMethod b : DepreciationMethod.values())
            if (String.valueOf(b.value).equals(text))
                return b;
        return null;
    }
}
