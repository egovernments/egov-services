package org.egov.contract;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.model.Revaluation;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RevaluationRequest   {
	
  @JsonProperty("RequestInfo")
  private RequestInfo requestInfo;

  @JsonProperty("Revaluation")
  @Valid
  private Revaluation revaluation;

}

