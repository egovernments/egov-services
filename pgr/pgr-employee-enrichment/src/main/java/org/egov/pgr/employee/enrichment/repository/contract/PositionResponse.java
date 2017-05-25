package org.egov.pgr.employee.enrichment.repository.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PositionResponse {

    @JsonProperty("deptdesig")
    private DepartmentDesignationResponse departmentDesignation;

}
