package org.egov.citizen.web.contract;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ServiceRequestSearchCriteria {
	
	private String serviceRequestId;
	
	private String userId;
	
	private String tenantId;
	
	private String sortBy;
	
	private String sortOrder;

}
