package org.egov.lams.model;

import javax.validation.constraints.NotNull;
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
	
	@NotNull
	private String name;
	
	@NotNull
	@JsonProperty("permanentAddress")
	private String address;
	
	@NotNull
	private Long mobileNumber;
	private String aadhaarNumber;
	private String pan;
	private String emailId;
}
