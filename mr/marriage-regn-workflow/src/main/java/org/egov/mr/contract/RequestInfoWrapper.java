package org.egov.mr.contract;

import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RequestInfoWrapper {
	
	@JsonProperty(value="RequestInfo")
	private RequestInfo requestInfo;
	
}
