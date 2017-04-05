package org.egov.eis.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.egov.common.contract.response.ResponseInfo;

import java.util.List;
import java.util.stream.Collectors;

public class DepartmentResponse {
	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo;

	@JsonProperty("Department")
	private List<Department> department;

    public DepartmentResponse(List<org.egov.eis.persistence.entity.Department> entityDepartments) {
        department = entityDepartments.stream()
                .map(Department::new)
                .collect(Collectors.toList());
    }
}
