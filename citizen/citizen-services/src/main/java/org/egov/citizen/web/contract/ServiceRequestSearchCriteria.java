package org.egov.citizen.web.contract;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ServiceRequestSearchCriteria {
	
	private String serviceRequestId;
	private Long userId;
	@NotNull
	private String tenantId;
	private String sortBy;
	private String sortOrder;

}
