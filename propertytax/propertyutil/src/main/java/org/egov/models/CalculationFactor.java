package org.egov.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalculationFactor {

	private Long id;	
		 
	@NotNull
	@Size(min=4,max=128)
	private String	tenantId;	

	@NotNull
	private String	factorCode;
		 
	@NotNull
	private String	factorType;
	
	@NotNull	
	private Double	factorValue;	
	
	@NotNull
	private String	fromDate;	
	
	@NotNull
	private String	toDate;	
		
	private AuditDetails auditDetails;
}
