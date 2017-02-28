package org.egov.workflow.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class User {
    
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("userName")
    private String userName = null;

    @JsonProperty("name")
    private String name = null;

}
