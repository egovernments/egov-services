package org.egov.lcms.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
