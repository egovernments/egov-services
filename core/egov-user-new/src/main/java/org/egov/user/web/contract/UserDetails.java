package org.egov.user.web.contract;

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
	private Long dob;
	private String altContactNumber;
	private String fatherName;
	private String husbandName;
	private String bloodGroup;
	private String pan;
	private String signature;
	private String identificationMark;
	private String photo;

}
