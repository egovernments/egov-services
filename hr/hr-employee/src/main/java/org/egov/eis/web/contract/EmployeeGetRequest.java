package org.egov.eis.web.contract;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeGetRequest {

	@NotNull
	Long id;
	
	@NotNull
	@Size(max=256)
	String tenantId;

}
