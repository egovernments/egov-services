package org.egov.wcms.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ServiceType {
    CHANGEOFOWNERSHIP("CHANGEOFOWNERSHIP"), CHANGEOFCONNECTIONTYPE("CHANGEOFCONNECTIONTYPE"), DISCONNECTION(
            "DISCONNECTION"), CHANGEOFUSAGE("CHANGEOFUSAGE"), NODUECERTIFICATE("NODUECERTIFICATE");

    private final String value;

    ServiceType(final String value) {
        this.value = value;
    }

    @JsonCreator
    public static ServiceType fromValue(final String passedValue) {
        for (final ServiceType obj : ServiceType.values())
            if (String.valueOf(obj.value).equals(passedValue.toUpperCase()))
                return obj;
        return null;
    }
}
