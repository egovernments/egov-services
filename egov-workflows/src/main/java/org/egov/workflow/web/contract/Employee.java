package org.egov.workflow.web.contract;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Employee {

	@JsonProperty("id")
	private Long id;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("code")
	private String code;
	
	@JsonProperty("username")
	private String username;
	
	@JsonProperty("assignments")
	private final List<Assignment> assignments = new ArrayList<Assignment>(0);
	
	

}
