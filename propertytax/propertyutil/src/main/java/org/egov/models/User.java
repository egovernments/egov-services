package org.egov.models;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

/**
 * User
 */

@Data
@Getter
@Setter
@ToString
public class User {

	private String id;

	private String username;

	private String password;

	private String salutation;

	private String name;

	private String gender;

	private String mobileNumber;

	private String emailId;

	private String altContactNumber;

	private String pan;

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

	private String type;

	private String title;

	private String guardian;

	private String guardianRelation;

	private String fatherOrHusbandName;

	private String bloodGroup;

	private String identificationMark;

	private String photo;

	private Integer createdBy;

	private String createdDate;

	private Integer lastModifiedBy;

	private String lastModifiedDate;

	private String otpReference;
	
	@NonNull
	private String tenantId;

	private List<Role> roles;

}
