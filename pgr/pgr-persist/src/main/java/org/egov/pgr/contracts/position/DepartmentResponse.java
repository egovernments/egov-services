package org.egov.pgr.contracts.position;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DepartmentResponse {

    @JsonProperty("id")
    private Long id;

    public Long getId() {
        return id;
    }
}
