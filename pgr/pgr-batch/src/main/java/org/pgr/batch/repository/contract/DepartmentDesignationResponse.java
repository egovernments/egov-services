package org.pgr.batch.repository.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DepartmentDesignationResponse {

    @JsonProperty("designation")
    private DesignationResponse designation;

    @JsonProperty("department")
    private Long department;
}
