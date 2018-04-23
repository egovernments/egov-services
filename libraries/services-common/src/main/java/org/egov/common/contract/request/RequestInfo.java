package org.egov.common.contract.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestInfo {
	
	@JsonProperty("apiId")
	private String apiId;

	@JsonProperty("ver")
	private String ver;

	@JsonProperty("locale")
	private String locale;

	@JsonProperty("ts")
	private Long ts;

	@JsonProperty("action")
	private String action;

	@JsonProperty("did")
	private String did;

	@JsonProperty("key")
	private String key;

	@JsonProperty("msgId")
	private String msgId;

	@JsonProperty("requesterId")
	private String requesterId;

	@JsonProperty("authToken")
	private String authToken;

	@JsonProperty("userInfo")
	private UserInfo userInfo;

	@JsonProperty("correlationId")
	private String correlationId;

}
