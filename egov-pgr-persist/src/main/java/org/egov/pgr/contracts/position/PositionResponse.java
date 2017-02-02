package org.egov.pgr.contracts.position;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PositionResponse {

    @JsonProperty("deptDesig")
    private DepartmentDesignationResponse departmentDesignation;

    public DepartmentDesignationResponse getDepartmentDesignation() {
        return departmentDesignation;
    }
}
