package org.egov.citizen.model;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IdGenerationReqWrapper {
	
	 @JsonProperty("RequestInfo")
	private RequestInfo requestInfo;
	private List<IdRequest> idRequests;
	

}
