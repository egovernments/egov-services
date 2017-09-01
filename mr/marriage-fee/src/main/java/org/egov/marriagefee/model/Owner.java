package org.egov.marriagefee.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Owner {

	@JsonProperty("tenantId")
	private String tenantId;

	@JsonProperty("id")
	private Long id;

	@JsonProperty("userName")
	private String userName;

	@JsonProperty("name")
	private String name;
	
	@JsonProperty("permanentAddress")
	private String permanentAddress;

	@JsonProperty("mobileNumber")
	private String mobileNumber;

	@JsonProperty("emailId")
	private String emailId;

	@JsonProperty("aadhaarNumber")
	private String aadhaarNumber;

}
