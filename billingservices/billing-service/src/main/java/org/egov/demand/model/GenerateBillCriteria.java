package org.egov.demand.model;

import org.hibernate.validator.constraints.Email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class GenerateBillCriteria {
	
	private String tenantId;
	
	private String demandId;
	
	private String consumerCode;
	
	private String businessService;
	
	@Email
	private String email;
	
	private String mobileNumber;

}
