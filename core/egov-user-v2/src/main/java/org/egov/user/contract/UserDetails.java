package org.egov.user.contract;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class UserDetails {
	
	@JsonProperty("uuid")
	private String uuid;

	@JsonProperty("salutation")
	@Size(min=2,max=5)
	private String salutation;

	@JsonProperty("firstName")
	@Size(min=2, max=32)
	private String firstName;

	@JsonProperty("middleName")
	@Size(min=2, max=32)
	private String middleName;

	@JsonProperty("lastName")
	@Size(min=2, max=32)
	private String lastName;

	@JsonProperty("aadhaarNumber")
	@Pattern(regexp="(^$|[0-9]{12})")
	private String aadhaarNumber;
	
	@JsonProperty("pwdExpiryDate")
	private Long pwdExpiryDate;

	@JsonProperty("gender")
	private String gender;

	@JsonProperty("dob")
	private Long dob;

	@JsonProperty("altContactNumber")
	private String altContactNumber;

	@JsonProperty("fatherName")
	private String fatherName;

	@JsonProperty("husbandName")
	private String husbandName;

	@JsonProperty("bloodGroup")
	private String bloodGroup;

	@JsonProperty("pan")
	@Size(max=10)
	private String pan;

	@JsonProperty("addresses")
	@Valid
	private List<Address> addresses;

	@JsonProperty("signature")
	private String signature;

	@JsonProperty("photo")
	private String photo;
	
	@JsonProperty("identificationMark")
	private String identificationMark;

}
