package org.egov.asset.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepreciationCriteria {
	
	public DepreciationCriteria(DepreciationCriteria depreciationCriteria){
		this.assetIds = depreciationCriteria.assetIds;
		this.financialYear = depreciationCriteria.financialYear;
		this.fromDate = depreciationCriteria.fromDate;
		this.toDate = depreciationCriteria.toDate;
		this.tenantId = depreciationCriteria.tenantId;
	}

	@JsonProperty("tenantId")
	private String tenantId;

	@JsonProperty("financialYear")
	private String financialYear;

	@JsonProperty("fromDate")
	private Long fromDate;

	@JsonProperty("toDate")
	private Long toDate;

	@JsonProperty("assetIds")
	private Set<Long> assetIds = new HashSet<>();
}
