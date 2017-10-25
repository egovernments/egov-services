package org.egov.lcms.models;

import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This object holds information about the legal case request
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LegalCaseRequest   {
  @JsonProperty("requestInfo")
  @Valid
  private RequestInfo requestInfo = null;

  @JsonProperty("legleCases")
  @Valid
  private List<LegalCase> legleCases = null; 
}

