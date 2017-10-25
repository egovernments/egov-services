package org.egov.lcms.models;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * RequestInfo should be used to carry meta information about the requests to
 * the server as described in the fields below. All eGov APIs will use
 * requestinfo as a part of the request body to carry this meta information.
 * Some of this information will be returned back from the server as part of the
 * ResponseInfo in the response body to ensure correlation.
 */

public class RequestInfo {
	@JsonProperty("apiId")
	@NotNull

	@Size(max = 128)
	private String apiId = null;

	@JsonProperty("ver")
	@NotNull
	@Size(max = 32)
	private String ver = null;

	@JsonProperty("ts")
	private Long ts = null;

	@JsonProperty("action")
	@Size(max = 32)
	private String action = null;

	@JsonProperty("did")
	private String did = null;

	@JsonProperty("key")
	private String key = null;

	@JsonProperty("msgId")
	@NotNull
	@Size(max = 256)
	private String msgId = null;

	@JsonProperty("requesterId")
	private String requesterId = null;

	@JsonProperty("authToken")
	private String authToken = null;

	@JsonProperty("userInfo")
	private UserInfo userInfo = null;

	@JsonProperty("correlationId")
	private String correlationId = null;

	
}
