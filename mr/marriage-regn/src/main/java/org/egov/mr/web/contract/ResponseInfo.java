package org.egov.mr.web.contract;

import java.time.LocalDate;

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
public class ResponseInfo {
	@JsonProperty("resMsgId")
	private String resMsgId = null;

	@JsonProperty("status")
	private String status = null;

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
