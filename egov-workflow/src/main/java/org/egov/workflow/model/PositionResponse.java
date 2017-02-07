package org.egov.workflow.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PositionResponse {

    private Long id;

    private String name;

    @JsonProperty("responseInfo")
    private ResponseInfo responseInfo = null;

}
