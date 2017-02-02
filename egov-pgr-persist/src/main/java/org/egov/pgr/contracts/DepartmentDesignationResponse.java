package org.egov.pgr.contracts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DepartmentDesignationResponse {

    @JsonProperty("designation")
    private DesignationResponse designation;

    public DesignationResponse getDesignation() {
        return designation;
    }
}
