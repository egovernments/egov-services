package org.egov.user.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * This is acting ID token of the authenticated user on the server. Any value
 * provided by the clients will be ignored and actual user based on authtoken
 * will be used on the server.
 */

@Setter
@Getter
@ToString
public class UserInfo {

	@JsonProperty("tenantId")
	@NotNull
	private String tenantId = null;

	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("username")
	@NotNull
	private String username = null;
	
	@JsonProperty("password")
	@NotNull
	private String password;

	@JsonProperty("mobile")
	private String mobile = null;

	@JsonProperty("email")
	private String email = null;

	@JsonProperty("primaryrole")
	private List<Role> primaryrole = new ArrayList<>();

	@JsonProperty("additionalroles")
	private List<TenantRole> additionalroles = new ArrayList<>();

}
