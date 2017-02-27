package org.egov.workflow.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Value {
    @JsonProperty("key")
    private String key;

    @JsonProperty("name")
    private String name;
}