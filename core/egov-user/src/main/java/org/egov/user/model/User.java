package org.egov.user.model;

import org.egov.user.model.enums.Gender;
import org.egov.user.model.enums.Type;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class User extends UserInfo {

	@JsonProperty("authToken")
	private String authToken = null;

	@JsonProperty("salutation")
	private String salutation = null;

	@JsonProperty("name")
	private String name = null;

	@JsonProperty("gender")
	private Gender gender = null;

	@JsonProperty("aadhaarNumber")
	private String aadhaarNumber = null;

	@JsonProperty("active")
	private Boolean active = null;

	@JsonProperty("pwdExpiryDate")
	private Long pwdExpiryDate = null;

	@JsonProperty("locale")
	private String locale = null;

	@JsonProperty("type")
	private Type type = null;

	@JsonProperty("accountLocked")
	private Boolean accountLocked = false;

	@JsonProperty("userDetails")
	private UserDetails userDetails = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;
	

}
