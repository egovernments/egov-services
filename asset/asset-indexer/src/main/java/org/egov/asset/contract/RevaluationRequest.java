package org.egov.asset.contract;

import org.egov.asset.model.Revaluation;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class RevaluationRequest {
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo = null;

	@JsonProperty("Revaluation")
	private Revaluation revaluation = null;

}
