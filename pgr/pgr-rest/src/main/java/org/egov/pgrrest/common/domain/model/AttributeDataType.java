package org.egov.pgrrest.common.domain.model;

import lombok.Getter;

public enum AttributeDataType {
    STRING("string"),
    INTEGER("integer"),
    DOUBLE("double"),
    DATE("date"),
    DATE_TIME("datetime"),
    SINGLE_VALUE_LIST("singlevaluelist"),
    MULTI_VALUE_LIST("multivaluelist"),
    SINGLE_FILE("file"),
    MULTI_FILE("multifile"),
    LONG("long"),
	TEXTAREA("textarea");

    @Getter
    private String name;

    AttributeDataType(String name) {
        this.name = name;
    }
}
