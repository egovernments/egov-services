package org.egov.pgrrest.common.domain.model;

public enum UserType {
    EMPLOYEE("EMPLOYEE"),
    CITIZEN("CITIZEN"),
    SYSTEM("SYSTEM_USER");

    private String value;

    UserType(String value) {
        this.value = value;
    }
}
