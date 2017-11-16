package org.egov.contract;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
  @NotNull
  private RequestInfo requestInfo;

  @JsonProperty("Revaluation")
  @Valid
  @NotNull
  private Revaluation revaluation;

}

