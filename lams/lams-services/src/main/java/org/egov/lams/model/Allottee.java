package org.egov.lams.model;

import java.util.List;

import lombok.*;
import org.egov.lams.model.enums.Gender;
import org.egov.lams.model.enums.UserType;
import org.egov.lams.web.contract.Role;

import com.fasterxml.jackson.annotation.JsonProperty;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@EqualsAndHashCode
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
