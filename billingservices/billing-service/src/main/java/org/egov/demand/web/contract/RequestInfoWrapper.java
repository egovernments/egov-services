package org.egov.demand.web.contract;

import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestInfoWrapper {
	
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;
}
