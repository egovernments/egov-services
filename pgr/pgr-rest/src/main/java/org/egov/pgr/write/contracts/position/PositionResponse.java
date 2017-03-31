package org.egov.pgr.write.contracts.position;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class PositionResponse {

    @JsonProperty("deptDesig")
    private DepartmentDesignationResponse departmentDesignation;

}
