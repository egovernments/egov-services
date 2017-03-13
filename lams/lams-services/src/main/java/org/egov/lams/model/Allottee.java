package org.egov.lams.model;

import javax.validation.constraints.NotNull;

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
	
	@NotNull
	private String name;
	
	@NotNull
	private String address;
	
	@NotNull
	private Long mobileNumber;
	private String aadhaarNumber;
	private String pan;
	private String emailId;

}
