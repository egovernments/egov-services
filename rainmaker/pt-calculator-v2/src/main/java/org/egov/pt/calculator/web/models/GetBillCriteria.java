package org.egov.pt.calculator.web.models;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetBillCriteria {
	
	@NotNull
	private String assessmentNumber;
	
	@Default
	private BigDecimal amountExpected = BigDecimal.ZERO;
	
	@NotNull
	private String propertyId;
	
	private String assessmentYear;
	
	@NotNull
	private String tenantId;
	
	private String billId;
	
}
