package org.egov.marriagefee.model;

import java.util.ArrayList;
import java.util.List;

import javax.management.relation.Role;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is acting ID token of the authenticated user on the server. Any value
 * provided by the clients will be ignored and actual user based on authtoken
 * will be used on the server. Author : Narendra
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
	@JsonProperty("tenantId")
	@NotNull
	private String tenantId = null;

	@JsonProperty("id")
	private Integer id = null;

	@JsonProperty("userName")
	@NotNull
	private String username = null;

	@JsonProperty("mobile")
	private String mobile = null;

	@JsonProperty("email")
	private String email = null;

	@JsonProperty("primaryrole")
	@NotNull
	private List<Role> primaryrole = new ArrayList<Role>();


}
