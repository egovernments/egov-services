package org.egov.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxPeriod {
	private Long id;
	
	@Size(min=4,max=128)
	@NotNull
	private String	tenantId;	
	
	@NotNull
	private String	fromDate;	
	
	@NotNull
	private String	toDate;	
	
	@Size(min=4,max=128)
	@NotNull
	private String	code;	
	
	@NotNull
	private String	periodType;
	
	@Size(min=4,max=64)
	private String	financialYear;

	private AuditDetails auditDetails;

}
