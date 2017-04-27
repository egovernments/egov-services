package org.egov.lams.model;

import java.util.List;

import org.egov.lams.model.enums.Gender;
import org.egov.lams.model.enums.UserType;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Allottee {

	private Long id;
	private String name;
	@JsonProperty("permanentAddress")
	private String address;
	private String mobileNumber;
	private String aadhaarNumber;
	private String pan;
	private String emailId;
	private String userName;
	private String password;
	private Boolean active;
	private UserType type;
	private Gender gender;
	private String tenantId; 
	@JsonProperty("roles")
    private List<Role> roles;
}
