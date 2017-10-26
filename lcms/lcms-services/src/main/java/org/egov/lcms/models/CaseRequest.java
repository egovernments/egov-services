package org.egov.lcms.models;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaseRequest {
	@JsonProperty("requestInfo")
	private RequestInfo requestInfo = null;

	@JsonProperty("cases")
	private List<Case> cases = null;
}