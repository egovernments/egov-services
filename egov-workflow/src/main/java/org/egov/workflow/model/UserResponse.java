package org.egov.workflow.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse {

    @Setter
    private Long id;

    @Setter
    private String name;

    @JsonProperty("responseInfo")
    private ResponseInfo responseInfo = null;


}
