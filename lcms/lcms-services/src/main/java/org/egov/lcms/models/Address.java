package org.egov.lcms.models;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {

	@Size(min = 4, max = 128)
	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("latitude")
	private Double latitude = null;

	@JsonProperty("longitude")
	private Double longitude = null;

	@JsonProperty("addressId")
	private String addressId = null;

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

	@Size(min = 6, max = 6)
	@JsonProperty("pincode")
	private String pincode = null;

	@JsonProperty("detail")
	private String detail = null;
}
