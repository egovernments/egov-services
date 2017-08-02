package org.egov.user.model;

import java.util.Date;
import java.util.List;

import org.egov.user.model.enums.BloodGroup;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UserDetails {

	@JsonProperty("firstName")
	private String firstName = null;

	@JsonProperty("middleName")
	private String middleName = null;

	@JsonProperty("lastName")
	private String lastName = null;

	@JsonProperty("dob")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date dob = null;

	@JsonProperty("altContactNumber")
	private String altContactNumber = null;

	@JsonProperty("fatherName")
	private String fatherName = null;

	@JsonProperty("husbandName")
	private String husbandName = null;

	@JsonProperty("bloodGroup")
	private BloodGroup bloodGroup = null;

	@JsonProperty("pan")
	private String pan = null;

	@JsonProperty("addresses")
	private List<Address> addresses;

	@JsonProperty("signature")
	private String signature = null;

	@JsonProperty("identificationMark")
	private String identificationMark = null;

	@JsonProperty("photo")
	private String photo = null;

}
