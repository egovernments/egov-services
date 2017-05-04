package org.egov.asset.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * Hold the Asset dynamic fields details as list of json object.
 */

@Setter
@Getter
@ToString
public class AttributeDefination   {
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

  @JsonProperty("url")
  private String url = null;

  @JsonProperty("order")
  private String order = null;

  @JsonProperty("columns")
  private List<AttributeDefination> columns = null;


}

