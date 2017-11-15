package org.egov.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
public class YearWiseDepreciation   {
	  @JsonProperty("depreciationRate")
	  private Integer depreciationRate = null;

	  @JsonProperty("financialYear")
	  private String financialYear = null;

	  @JsonProperty("assetId")
	  private Long assetId = null;

	  @JsonProperty("usefulLifeInYears")
	  private Long usefulLifeInYears = null;
 }