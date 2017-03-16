package org.egov.lams.model;

import javax.validation.constraints.NotNull;

<<<<<<< d852f719e2afdbe653d81b070e62b33569ebf856
import com.fasterxml.jackson.annotation.JsonProperty;
=======
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

>>>>>>> setting removed
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
