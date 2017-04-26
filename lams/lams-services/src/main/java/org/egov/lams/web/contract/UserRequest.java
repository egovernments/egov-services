package org.egov.lams.web.contract;

import java.util.List;

import org.egov.lams.model.Allottee;
import org.egov.lams.model.enums.Gender;
import org.egov.lams.model.enums.UserType;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
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
	
	public static UserRequest buildUserRequestFromAllotte(Allottee allottee) {
		
		return  UserRequest.builder()
									.aadhaarNumber(allottee.getAadhaarNumber())
									.active(allottee.getActive())
									.address(allottee.getAddress())
									.emailId(allottee.getEmailId())
									.gender(allottee.getGender())
									.id(allottee.getId())
									.mobileNumber(allottee.getMobileNumber())
									.name(allottee.getName())
									.pan(allottee.getPan())
									.password(allottee.getPassword())
									.roles(allottee.getRoles())
									.tenantId(allottee.getTenantId())
									.type(allottee.getType())
									.userName(allottee.getUserName())
									.build();				
	}
}
