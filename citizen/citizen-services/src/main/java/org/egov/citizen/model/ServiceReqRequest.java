package org.egov.citizen.model;

import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ServiceReqRequest {

	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;
	private ServiceReq serviceReq;

}
