package org.egov.demand.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnerUserDetails   {
	
	  private String firstName;

	  private String middleName;

	  private String lastName;

	  private Long dob;

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
}