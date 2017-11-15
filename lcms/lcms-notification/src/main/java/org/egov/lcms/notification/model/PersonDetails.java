package org.egov.lcms.notification.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonDetails {
	
	@Size(min = 1, max = 100)
	@JsonProperty("name")
	private String name ;

	@JsonProperty("code")
	private String code ;

	@Size(min = 1, max = 100)
	@JsonProperty("agencyName")
	private String agencyName ;
	
	@JsonProperty("agencyCode")
	private String agencyCode ;

	@NotNull	
	@JsonProperty("title")
	private String title ;

	@NotNull	
	@JsonProperty("firstName")
	private String firstName ;

	@NotNull	
	@JsonProperty("secondName")
	private String secondName ;

	@NotNull	
	@JsonProperty("lastName")
	private String lastName ;

	@NotNull	
	@JsonProperty("address")
	private String address ;

	@NotNull	
	@JsonProperty("contactNo")
	private String contactNo ;
	
	@JsonProperty("vatTinNo")
	private String vatTinNo ;
	
	@NotNull	
	@Size(min = 12, max = 12)
	@JsonProperty("aadhar")
	private String aadhar ;
	
	@NotNull	
	@JsonProperty("gender")
	private String gender ;
	
	@NotNull	
	@JsonProperty("age")
	private String age ;
	
	@NotNull
	@JsonProperty("dob")
	private Long dob ;
	
	@NotNull	
	@Size(min = 10, max = 10)
	@JsonProperty("mobileNumber")
	private String mobileNumber ;

	@NotNull	
	@Email
	@JsonProperty("emailId")
	private String emailId ;

	@NotNull	
	@Size(min = 10, max = 10)
	@JsonProperty("pan")
	private String pan ;
	
	@NotNull
	@Size(min = 4, max = 128)
	@JsonProperty("tenantId")
	private String tenantId;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;

}
