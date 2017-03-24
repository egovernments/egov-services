package org.egov.asset.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
/**
 * Hold the Asset dynamic fields details as list of json object.
 */
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
public class AssetCategoryCustomFields   {
  @JsonProperty("name")
  private String name = null;

  @JsonProperty("type")
  private String type = null;

  @JsonProperty("isActive")
  private Boolean isActive = null;

  @JsonProperty("isMandatory")
  private Boolean isMandatory = null;

  @JsonProperty("values")
  private String values = null;

  @JsonProperty("localText")
  private String localText = null;

  @JsonProperty("regExFormate")
  private String regExFormate = null;

}

