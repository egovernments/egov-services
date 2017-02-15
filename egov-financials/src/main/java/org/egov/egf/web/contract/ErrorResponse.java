package org.egov.egf.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorResponse {

	@JsonProperty("response_info")
	private ResponseInfo responseInfo;

	@JsonProperty("error")
	private Error error;

	
}
