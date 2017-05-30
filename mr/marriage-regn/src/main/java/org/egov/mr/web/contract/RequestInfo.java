package org.egov.mr.web.contract;

import java.time.LocalDate;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Builder
public class RequestInfo {

	@JsonProperty("action")
	private String action = null;

	@JsonProperty("did")
	private String did = null;

	@JsonProperty("msgId")
	private String msgId = null;

	@JsonProperty("requesterId")
	private String requesterId = null;

	@JsonProperty("authToken")
	private String authToken = null;

	@JsonProperty("apiId")
	private String apiId = null;

	@JsonProperty("ver")
	private String ver = null;

	@JsonProperty("ts")
	private String ts = null;

	@JsonProperty("key")
	private String key = null;

	@JsonProperty("tenantId")
	private String tenantId = null;
}
