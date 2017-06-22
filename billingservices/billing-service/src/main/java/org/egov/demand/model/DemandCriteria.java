package org.egov.demand.model;

import java.math.BigDecimal;
import java.util.Set;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DemandCriteria {

	@NotNull
	private String tenantId; 
	
	private Set<String> demandId;
	
	private String consumerCode;
	
	private String businessService;
	
	private BigDecimal demandFrom;
	
	private BigDecimal demandTo;
	
	private String type;
	
	private String mobileNumber;
	
	private String email;
}
