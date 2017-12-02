package org.egov.swm.domain.enums;

import lombok.Getter;
import lombok.Setter;

public enum MaintenanceType {
    MAINTENANCE("MAINTENANCE"), REPAIR("REPAIR");

    @Getter
    @Setter
    private String code;

    MaintenanceType(final String code) {
        this.code = code;
    }
}
