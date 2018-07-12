package org.egov.pt.calculator.web.models;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetBillCriteria {
	
	@NotNull
	private String assessmentNumber;
	
	@NotNull
	private String propertyId;
	
	private String assessmentYear;
	
	@NotNull
	private String tenantId;
	
	private String billId;
	
}
