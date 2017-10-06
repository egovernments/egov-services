package org.egov.models;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Address Author : Narendra
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Address {

	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("latitude")
	private Double latitude = null;

	@JsonProperty("longitude")
	private Double longitude = null;

	@JsonProperty("addressNumber")
	private String addressNumber = null;

	@JsonProperty("addressLine1")
	private String addressLine1 = null;

	@JsonProperty("addressLine2")
	private String addressLine2 = null;

	@JsonProperty("landmark")
	private String landmark = null;

	@JsonProperty("city")
	private String city = null;

	@JsonProperty("pincode")
	private String pincode = null;

	@JsonProperty("detail")
	private String detail = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

	@Size(min = 1, max = 64)
	@JsonProperty("surveyNo")
	private String surveyNo;

	@Size(min = 1, max = 128)
	@JsonProperty("plotNo")
	private String plotNo;
}
