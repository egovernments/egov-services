package org.egov.asset.contract;

import javax.validation.Valid;

import org.egov.asset.model.DepreciationCriteria;
import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepreciationRequest {
	
	@JsonProperty("RequestInfo")
	@Valid
	private RequestInfo requestInfo;

	@JsonProperty("Depreciation")
	@Valid
	private DepreciationCriteria depreciationCriteria;
}
