package org.egov.pgrrest.read.web.contract;

import java.util.HashSet;
import java.util.Set;

import org.egov.pgrrest.common.domain.model.Role;

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
	
	@JsonProperty("roles")
	private Set<Role> roles = new HashSet<>();

	@JsonProperty("tenantId")
    private String tenantId;
}
