package org.egov.pgr.common.repository.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Employee {

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

    @Size(max=10)
    private String mobileNumber;

    @Size(min=5, max=128)
    private String emailId;
}
