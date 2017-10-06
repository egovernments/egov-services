package org.egov.models;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Infrequently used details for the user Author : Narendra
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetails {

	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("userName")
	@Size(min = 1, max = 34)
	private String userName = null;

	@JsonProperty("name")
	@Size(min = 3, max = 100)
	private String name = null;

	@JsonProperty("mobileNumber")
	@Size(max = 10)
	private String mobileNumber = null;

	@JsonProperty("emailId")
	@Size(max = 128)
	private String emailId = null;

	@JsonProperty("locale")
	@Size(max = 10)
	private String locale = null;

	@JsonProperty("type")
	@NotNull
	@Size(max = 20)
	private String type = null;

	@JsonProperty("roles")
	@Valid
	private List<Role> roles = new ArrayList<Role>();

	@JsonProperty("actions")
	@Valid
	private List<Action> actions = new ArrayList<Action>();

	@JsonProperty("active")
	private Boolean active = null;

	@JsonProperty("tenantId")
	private Boolean tenantId = null;
}
