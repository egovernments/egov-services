package org.egov.models;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * User
 */

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {

	private String id;
	
	@Length(min=1, max=64)
	private String username;

	private String password;
	
	@Length(min=1, max=5)
	private String salutation;
	
	@Length(min=1, max= 100)
	private String name;
	
	@Length(min=1, max=8)
	private String gender;
	
	@Length(min=1, max= 10)
	private String mobileNumber;
	
	@Length(min=1, max= 128)
	private String emailId;

	private String altContactNumber;
	
	@Length(min=10, max= 10)
	private String pan;
	
	@Length(min=12, max= 12)
	private String aadhaarNumber;

	private String permanentAddress;

	private String permanentPincode;

	private String permanentCity;

	private String correspondenceCity;

	private String correspondencePincode;

	private String correspondenceAddress;

	private Boolean active;

	private String dob;

	private String locale;

	private Boolean accountLocked;

	private String signature;
	
	@Length(min=1, max= 16)
	private String type;
	
	@Length(min=1, max=8)
	private String title;
	
	@Length(min=1, max= 100)
	private String guardian;
	
	@Length(min=1, max=32)
	private String guardianRelation;

	private String fatherOrHusbandName;

	private String bloodGroup;

	private String identificationMark;

	private String photo;

	private String otpReference;
	
	@NotNull
	@Length(min=4, max= 128)
	private String tenantId;

	@Valid
	private List<Role> roles;

}
