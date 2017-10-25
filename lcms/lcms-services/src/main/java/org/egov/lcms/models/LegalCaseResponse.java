package org.egov.lcms.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This object holds information about the legal case response
 */


public class LegalCaseResponse   {
  @JsonProperty("responseInfo")
  private ResponseInfo responseInfo = null;

  @JsonProperty("legleCases")
  private List<LegalCase> legleCases = null;

  
}

