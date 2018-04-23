package org.egov.common.contract.request;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

	@JsonProperty("tenantId")
	@NotNull
	private String tenantId;

	@JsonProperty("id")
	private String id;
	
	@JsonProperty("type")
	@NotNull
	@Size(max=20)
	private Type type;

	@JsonProperty("userName")
	@NotNull
	@Size(min=2,max=32)
	private String userName;

	@JsonProperty("password")
	private String password;
	
	@JsonProperty("locale")
	@Size(min=2, max=16)
	private String locale;

	@JsonProperty("idToken")
	private String idToken;

	@JsonProperty("mobile")
	private String mobile;

	@JsonProperty("email")
	private String email;

	@JsonProperty("primaryrole")
	@NotNull
	@Valid
	private List<Role> primaryrole;

	@JsonProperty("additionalroles")
	private List<TenantRole> additionalroles;

}
