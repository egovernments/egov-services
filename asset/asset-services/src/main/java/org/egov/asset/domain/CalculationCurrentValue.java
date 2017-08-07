package org.egov.asset.domain;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalculationCurrentValue {

	private Long id;
	
	private String tenantId;
	
	private Long assetId;
	
	private BigDecimal currentAmountBeforeSeptember;
	
	private BigDecimal currentAmountAfterSeptember;
	//private String assetTranType;
}
