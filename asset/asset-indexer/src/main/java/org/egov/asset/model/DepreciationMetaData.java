package org.egov.asset.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
/**
 * DepreciationMetaData
 */
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
public class DepreciationMetaData   {
  
  @JsonProperty("depreciationRate")
  private Long depreciationRate = null;

  @JsonProperty("financialYear")
  private String financialYear = null;

  @JsonProperty("usefulLifeInYears")
  private Long usefulLifeInYears = null;
}

