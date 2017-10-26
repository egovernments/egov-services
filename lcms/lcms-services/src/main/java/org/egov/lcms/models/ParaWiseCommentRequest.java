package org.egov.lcms.models;

import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This object holds information about the para wise comments
 */
public class ParaWiseCommentRequest   {
  @JsonProperty("requestInfo")
  @Valid
  private RequestInfo requestInfo = null;

  @JsonProperty("parawiseComments")
  @Valid
  private List<ParaWiseComment> parawiseComments = null;

 
}

