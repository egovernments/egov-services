package org.egov.wcms.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ServiceChargeType {
    FLAT("FLAT"), SLAB("SLAB"), PERCENTAGEFLAT("PERCENTAGEFLAT"), PERCENTAGESLAB("PERCENTAGESLAB");

    private final String value;

    ServiceChargeType(final String value) {
        this.value = value;
    }

    @JsonCreator
    public static ServiceChargeType fromValue(final String passedValue) {
        for (final ServiceChargeType obj : ServiceChargeType.values())
            if (String.valueOf(obj.value).equals(passedValue.toUpperCase()))
                return obj;
        return null;
    }

}
