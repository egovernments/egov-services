package org.egov.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepreciationInputs {
	
	private String assetCode;

	private Long assetId;
	
	private Long lastDepreciationDate;

	private String tenantId;

	private BigDecimal grossValue;
	
	private BigDecimal originalValue;

	private BigDecimal accumulatedDepreciation;

	private Long assetCategory;

	private Double depreciationRate;

	private BigDecimal currentValue;

	private BigDecimal depreciationSum;
	
	private Long openingDate;
	
	private String assetaccount;
	
	private String accumulateddepreciationaccount;
	
	private String revaluationreserveaccount;
	
	private String depreciationexpenseaccount;
}
