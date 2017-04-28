package org.egov.asset.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * Additional information of the asset.Hold the data for dynamic custom field in JSON format. There key and value will be LABEL NAME and USER INPUT DATA respactively.
 */

@Setter
@Getter
@ToString
public class Attributes   {
  @JsonProperty("key")
  private String key = null;

  @JsonProperty("type")
  private String type = null;

  @JsonProperty("value")
  private Object value = null;

}

