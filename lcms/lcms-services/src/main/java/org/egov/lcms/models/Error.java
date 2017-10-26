package org.egov.lcms.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Error object will be returned as a part of reponse body in conjunction with ResponseInfo as part of ErrorResponse whenever the request processing status in the ResponseInfo is FAILED. HTTP return in this scenario will usually be HTTP 400.
 */
public class Error   {
  @JsonProperty("code")
  private String code = null;

  @JsonProperty("message")
  private String message = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("params")
  private List<String> params = null;
}

