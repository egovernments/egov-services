package org.egov.commons.web.contract;

import org.egov.commons.model.Uom;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UomRequest {
	
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;
	
	@JsonProperty("uom")
	private Uom uom;

}
