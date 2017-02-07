package org.egov.pgr.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Error   {
    @JsonProperty("code")
    private int code;

    @JsonProperty("message")
    private String message;

    @JsonProperty("description")
    private String description;

    @JsonProperty("fields")
    private Object fields;

    public Error(int errorCode, String message) {
        this.code = errorCode;
        this.message = message;
    }

}

