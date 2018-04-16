package org.egov.eis.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Sequence {
	DESIGNATIONSEQUENCS("seq_egeis_designation");

    private String value;

    Sequence(final String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static Sequence fromValue(final String text) {
        for (final Sequence b : Sequence.values())
            if (String.valueOf(b.value).equals(text))
                return b;
        return null;
    }
}
