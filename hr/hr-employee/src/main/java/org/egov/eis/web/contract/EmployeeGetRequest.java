package org.egov.eis.web.contract;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class EmployeeGetRequest {

	@NotNull
	Long id;
	
	@NotNull
	@Size(min=1, max=256)
	String tenantId;

}
