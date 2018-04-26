package org.egov.user.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Address {
	
	@JsonProperty("uuid")
	private String uuid;
	
	@JsonProperty("tenantId")
	private String tenantId;

	@JsonProperty("latitude")
	private Double latitude;

	@JsonProperty("longitude")
	private Double longitude;

	@JsonProperty("addressId")
	private String addressId;

	@JsonProperty("addressNumber")
	private String addressNumber;

	@JsonProperty("type")
	private String type;

	@JsonProperty("addressLine1")
	private String addressLine1;

	@JsonProperty("addressLine2")
	private String addressLine2;

	@JsonProperty("landmark")
	private String landmark;

	@JsonProperty("city")
	private String city;

	@JsonProperty("pincode")
	private String pincode;

	@JsonProperty("detail")
	private String detail;
}
