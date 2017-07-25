package org.egov.collection.model.enums;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CollectionType {
    COUNTER("Counter"),
    FIELD("Field"),
    ONLINE("Online");

    
    private String value;
    
    CollectionType(final String value) {
        this.value = value;
    }
    
    @Override
    @JsonValue
    public String toString() {
        return StringUtils.capitalize(name());
    }

    public String getValue() {
        return value;
    }

}
