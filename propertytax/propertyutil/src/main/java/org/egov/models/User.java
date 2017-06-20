package org.egov.models;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User
 * 
 * Author : Narendra
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
	@JsonProperty("tenantId")
	@NotNull
	private String tenantId = null;

	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("userName")
	private String userName = null;

	@JsonProperty("authToken")
	private String authToken = null;

	@JsonProperty("salutation")
	@Size(max = 5)
	private String salutation = null;

	@JsonProperty("name")
	@NotNull
	@Size(min = 3, max = 100)
	private String name = null;

	@JsonProperty("gender")
	@NotNull
	private String gender = null;

	@JsonProperty("mobileNumber")
	@NotNull
	@Size(max = 10)
	private String mobileNumber = null;

	@JsonProperty("emailId")
	@Size(max = 128)
	private String emailId = null;

	@JsonProperty("aadhaarNumber")
	@Pattern(regexp = "[0-9]")
	@Size(max = 12)
	private String aadhaarNumber = null;

	@JsonProperty("active")
	@NotNull
	private Boolean active = null;

	@JsonProperty("pwdExpiryDate")
	private Long pwdExpiryDate = null;

	@JsonProperty("locale")
	@NotNull
	@Size(max = 5)
	private String locale = null;

	@JsonProperty("type")
	@NotNull
	@Size(max = 20)
	private String type = null;

	@JsonProperty("accountLocked")
	private Boolean accountLocked = false;

	@JsonProperty("roles")
	@Valid
	private List<Role> roles = new ArrayList<Role>();

	@JsonProperty("userDetails")
	@Valid
	private UserDetails userDetails = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;
}
