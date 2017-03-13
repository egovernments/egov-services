package org.egov.audit.web.contract;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestInfo {
	@JsonProperty("api_id")
	private String apiId;

	@JsonProperty("ver")
	private String ver;

	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
	@JsonProperty("ts")
	private Date ts;

	@JsonProperty("action")
	@Setter
	private String action;

	@JsonProperty("did")
	private String did;

	@JsonProperty("key")
	private String key;

	@JsonProperty("msg_id")
	@Setter
	private String msgId;

	@JsonProperty("requester_id")
	@Setter
	private String requesterId;

	@JsonProperty("auth_token")
	private String authToken;
}