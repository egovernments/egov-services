package org.egov.demand.model;

import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillSearchCriteria {

	@NotNull
	private String tenantId;
	
	private Set<String> billId;
	
	private Boolean isActive;
	
	private Boolean isCancelled;
	
	private String service;

	private String billType;
	
	private String consumerCode;
	
	private Long size;
	
	private Long offset;
}
