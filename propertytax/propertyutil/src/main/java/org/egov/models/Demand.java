package org.egov.models;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Demand {

	private Integer id;

	@NotNull
    private String	tenantId;
	
	@NotNull
	private String consumerCode;	

	private String consumerType;	
	
	@NotNull
	private String businessService;

	@NotNull
	private User owner;	

	@NotNull
	private String taxPeriodFrom;
	
	@NotNull
	private String taxPeriodTo;
	
	@NotNull
	private DemandDetails demandDetails;	

	private AuditDetails auditDetails;

	private Double minimumAmountPayable;	

}
