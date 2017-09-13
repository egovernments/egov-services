package org.egov.marriagefee.model;

import javax.validation.constraints.NotNull;

import org.egov.marriagefee.model.enums.MaritalStatus;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Builder
public class MarryingPerson {
	
	@NotNull
	private Long id;
	
	@NotNull
	private String name;

	@NotNull
	private String parentName;

	@NotNull
	private String street;

	@NotNull
	private String locality;

	@NotNull
	private String city;

	@NotNull
	private Long dob;

	@NotNull
	private MaritalStatus status;

	private String aadhaar;

	@NotNull
	private String mobileNo;

	private String email;

	@NotNull
	private Long religion;
	
	private String religionPractice;

	@NotNull
	private String education;

	private String occupation;

	private String handicapped;

	@NotNull
	private String residenceAddress;

	@NotNull
	private String photo;

	@NotNull
	private String nationality;
	
	private String officeAddress;
}
