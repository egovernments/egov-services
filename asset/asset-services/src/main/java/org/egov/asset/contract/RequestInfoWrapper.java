package org.egov.asset.contract;

import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class RequestInfoWrapper {
	
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;

}
