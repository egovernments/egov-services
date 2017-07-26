package org.egov.pgrrest.common.domain.model;

import lombok.Getter;

public enum GroupConstraintType {
    AT_LEAST_ONE_REQUIRED("atleastonerequired"),
    ALL_REQUIRED("allrequired");

    @Getter
    private final String code;

    GroupConstraintType(String code) {
        this.code = code;
    }
}

