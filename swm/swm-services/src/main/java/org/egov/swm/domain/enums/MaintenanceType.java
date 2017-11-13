package org.egov.swm.domain.enums;


import lombok.Getter;

public enum MaintenanceType {
    MAINTENANCE("MAINTENANCE"),
    REPAIR("REPAIR");

    @Getter
    private String code;

    MaintenanceType(String code) {
        this.code = code;
    }
}
