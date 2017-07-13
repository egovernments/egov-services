package org.egov.pgrrest.common.domain.model;

import lombok.Getter;

public enum UserType {
    EMPLOYEE("EMPLOYEE"),
    CITIZEN("CITIZEN"),
    SYSTEM("SYSTEM_USER");

    @Getter
    private String value;

    UserType(String value) {
        this.value = value;
    }
}
