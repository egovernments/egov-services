package org.egov.demand.model;

import java.util.List;

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
	
	private String demandId;
	
	private List<String> billId;
	
	private boolean isActive;
	
	private boolean isCancelled;

	private String billType;
	
	private String consumerCode;
}
