package org.egov.common.contract.response;

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
public class ResponseInfo {
	
	@JsonProperty("apiId")
	private String apiId;

	@JsonProperty("ver")
	private String ver;

	@JsonProperty("ts")
	private Long ts;

	@JsonProperty("resMsgId")
	private String resMsgId;

	@JsonProperty("msgId")
	private String msgId;
}
