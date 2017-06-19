package org.egov.pgr.common.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
class Employee {

	@JsonProperty("id")
	private Long id;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("code")
	private String code;
	
	@JsonProperty("userName")
	private String username;
	
	@JsonProperty("assignments")
	private List<Assignment> assignments = new ArrayList<Assignment>();

	@JsonProperty("tenantId")
	private String tenantId;

    private String mobileNumber;

    private String emailId;
}
