package org.egov.lcms.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This object holds information about the opinion response
 */
public class OpinionResponse   {
  @JsonProperty("responseInfo")
  private ResponseInfo responseInfo = null;

  @JsonProperty("opinions")
  private List<Opinion> opinions = null;

 
}

