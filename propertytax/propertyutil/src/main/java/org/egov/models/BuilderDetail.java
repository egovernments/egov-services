package org.egov.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BuilderDetail {

	@JsonProperty("certificateNumber")
	private String certificateNumber = null;

	@JsonProperty("certificateCompletionDate")
	private String certificateCompletionDate = null;

	@JsonProperty("certificateReceiveDate")
	private String certificateReceiveDate = null;

	@JsonProperty("agencyName")
	private String agencyName = null;

	@JsonProperty("licenseType")
	private String licenseType = null;

	@JsonProperty("licenseNumber")
	private String licenseNumber = null;

}
