package org.egov.asset.model;

import javax.validation.constraints.NotNull;

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

	@NotNull
	private Double depreciationRate;
	@NotNull
	private String financialYear;
	
	private Long assetId;
	
	private Long usefulLifeInYears;
}
