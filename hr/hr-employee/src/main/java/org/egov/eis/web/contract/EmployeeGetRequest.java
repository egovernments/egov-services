package org.egov.eis.web.contract;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeGetRequest {

	@NotNull
	Long id;
	
	@NotNull
	String tenantId;

}
