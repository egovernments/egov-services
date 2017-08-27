package org.egov.wcms.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum GapcodeLastMonths {

	LASTMONTH("LAST MONTH"), LAST2MONTHS("LAST 2 MONTHS"), LAST3MONTHS(
            "LAST 3 MONTHS"), LAST4MONTHS("LAST 4 MONTHS"), LAST5MONTHS(
                    "LAST 5 MONTHS"), LAST6MONTHS("LAST 6 MONTHS"), LAST7MONTHS(
                    		"LAST 7 MONTHS"), LAST8MONTHS("LAST 8 MONTHS"), LAST9MONTHS("LAST 9 MONTHS");

    private final String value;

    GapcodeLastMonths(final String value) {
        this.value = value;
    }


    public String getValue() {
        return value;
    }


    @JsonCreator
    public static GapcodeLastMonths fromValue(final String passedValue) {
        for (final GapcodeLastMonths obj : GapcodeLastMonths.values())
            if (String.valueOf(obj.value).equals(passedValue.toUpperCase()))
                return obj;
        return null;
    }


}
