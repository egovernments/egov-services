package org.egov.contract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Role {

    @JsonProperty("name")
    private String name;

    public void setName(String name) {
        this.name = name;
    }
}
