package org.egov.asset.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class YearWiseDepreciation {

	private Double depreciationRate;
	
	private String financialYear;
	
	private Long assetId;
	
	private Long usefulLifeInYears;
}
