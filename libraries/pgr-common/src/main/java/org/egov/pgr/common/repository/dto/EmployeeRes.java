package org.egov.pgr.common.repository.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRes {

	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo = null;

	@JsonProperty("Employee")
	private List<Employee> employees = new ArrayList<Employee>();

	public org.egov.pgr.common.model.Employee toDomain() {
	    if(CollectionUtils.isEmpty(employees)) {
	        return null;
        }
        final Employee firstEmployee = employees.get(0);
        return new org.egov.pgr.common.model.Employee(firstEmployee.getEmailId(), firstEmployee.getMobileNumber());
    }

}
