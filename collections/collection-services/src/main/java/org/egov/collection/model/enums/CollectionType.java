package org.egov.collection.model.enums;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CollectionType {
    COUNTER("Counter"),
    FIELD("Field"),
    ONLINE("Online");

    
    private String name;
    
    CollectionType(final String name) {
        this.name = name;
    }
    
    @Override
    @JsonValue
    public String toString() {
        return StringUtils.capitalize(name());
    }

    public String getName() {
        return name;
    }

}
