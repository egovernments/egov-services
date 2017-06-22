package org.egov.demand.model;

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
	
	private String demandId;
	
	private String consumerCode;
	
	private String businessService;
	
	private Double demandFrom;
	
	private Double demandTo;
	
	//FIXME WHAT IS THIS FIELD 
	private String type;
	
	private String mobileNumber;
	
	private String email;
	
}
