package org.egov.demand.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Owner {

	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("userName")
	private String userName = null;

	@JsonProperty("name")
	private String name = null;

	@JsonProperty("gender")
	private String gender = null;

	@JsonProperty("mobileNumber")
	private String mobileNumber = null;

	@JsonProperty("emailId")
	private String emailId = null;

	@JsonProperty("aadhaarNumber")
	private String aadhaarNumber = null;

}
