package org.egov.swm.domain.model;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DumpingGround {

	@JsonProperty("code")
	private String code;

	@JsonProperty("tenantId")
	@Length(min = 1, max = 128)
	@NotNull
	private String tenantId;

	@NotNull
	@JsonProperty("siteDetails")
	private SiteDetails siteDetails;

	@JsonProperty("isProcessingSite")
	private Boolean isProcessingSite;

}
