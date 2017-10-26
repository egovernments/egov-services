package org.egov.lcms.models;
import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This object holds information about the opinion request
 */


public class OpinionRequest   {
  @JsonProperty("requestInfo")
  @Valid
  private RequestInfo requestInfo = null;

  @JsonProperty("opinions")
  @Valid
  private List<Opinion> opinions = null;
}

