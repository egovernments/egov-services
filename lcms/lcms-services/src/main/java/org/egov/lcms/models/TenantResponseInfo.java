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
@AllArgsConstructor
@NoArgsConstructor
public class TenantResponseInfo {
	@JsonProperty("apiId")
	@NotNull
	@Size(max = 128)
	private String apiId = null;

	@JsonProperty("ver")
	@NotNull
	@Size(max = 32)
	private String ver = null;

	@JsonProperty("ts")
	@NotNull
	private String ts = null;

	@JsonProperty("resMsgId")
	private String resMsgId = null;

	@JsonProperty("msgId")
	@Size(max = 256)
	private String msgId = null;

	@JsonProperty("status")
	@NotNull
	private String status = null;

}
