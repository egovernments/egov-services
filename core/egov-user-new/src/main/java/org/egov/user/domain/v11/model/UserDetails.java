package org.egov.user.domain.v11.model;

import java.util.Date;
import java.util.List;

import org.egov.user.domain.model.Address;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDetails {

	private String firstName;
	private String middleName;
	private String lastname;
	private Date dob;
	private String altContactNumber;
	private String fatherName;
	private String husbandName;
	private String bloodGroup;
	private String pan;
	private String permanentAddress;
	private String permanentCity;
	private String permanentPincode;
	private String correspondenceCity;
	private String correspondencePincode;
	private String correspondenceAddress;
	private String signature;
	private String identificationMark;
	private String photo;
	private List<Address> addresses;

}
