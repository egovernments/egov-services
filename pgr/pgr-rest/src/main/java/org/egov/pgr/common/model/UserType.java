package org.egov.pgr.common.model;

public enum UserType {
    EMPLOYEE("EMPLOYEE"),
    CITIZEN("CITIZEN"),
    SYSTEM("SYSTEM");

    private String value;

    UserType(String value) {
        this.value = value;
    }
}
