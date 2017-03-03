package org.egov.pgr.contracts.position;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class DepartmentDesignationResponse {

    @JsonProperty("designation")
    private DesignationResponse designation;

    @JsonProperty("department")
    private DepartmentResponse department;
}
