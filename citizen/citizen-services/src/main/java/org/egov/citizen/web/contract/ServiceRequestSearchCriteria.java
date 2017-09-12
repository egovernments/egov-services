package org.egov.citizen.web.contract;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ServiceRequestSearchCriteria {
	
	@NotNull
	private String tenantId;
	private String serviceRequestId;
	private Long userId;
	private String serviceCode;
	private String consumerCode;
	private String assignedTo;
	private String status;
	private String sortBy;
	private String sortOrder;
	private Boolean anonymous;

}
	