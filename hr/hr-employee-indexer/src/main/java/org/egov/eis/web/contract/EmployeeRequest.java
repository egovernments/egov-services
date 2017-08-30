package org.egov.eis.web.contract;

import lombok.*;
import org.egov.common.contract.request.RequestInfo;
import org.egov.eis.model.Employee;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRequest {

	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;

	@JsonProperty("Employee")
	private Employee employee;

}


