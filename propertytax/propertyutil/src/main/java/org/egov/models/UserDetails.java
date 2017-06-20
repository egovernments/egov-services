package org.egov.models;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Infrequently used details for the user Author : Narendra
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetails {
	@JsonProperty("firstName")
	@Size(min = 1, max = 32)
	private String firstName = null;

	@JsonProperty("middleName")
	@Size(min = 1, max = 32)
	private String middleName = null;

	@JsonProperty("lastName")
	@Size(min = 1, max = 32)
	private String lastName = null;

	@JsonProperty("dob")
	private String dob = null;

	@JsonProperty("altContactNumber")
	@Size(max = 16)
	private String altContactNumber = null;

	@JsonProperty("fatherName")
	@Size(max = 100)
	private String fatherName = null;

	@JsonProperty("husbandName")
	@Size(max = 100)
	private String husbandName = null;

	@JsonProperty("bloodGroup")
	@Size(max = 3)
	private String bloodGroup = null;

	@JsonProperty("pan")
	@Size(max = 10)
	private String pan = null;

	@JsonProperty("permanentAddress")
	@Size(max = 300)
	private String permanentAddress = null;

	@JsonProperty("permanentCity")
	@Size(max = 300)
	private String permanentCity = null;

	@JsonProperty("permanentPincode")
	@Size(max = 6)
	private String permanentPincode = null;

	@JsonProperty("correspondenceCity")
	@Size(max = 50)
	private String correspondenceCity = null;

	@JsonProperty("correspondencePincode")
	@Size(max = 6)
	private String correspondencePincode = null;

	@JsonProperty("correspondenceAddress")
	@Size(max = 300)
	private String correspondenceAddress = null;

	@JsonProperty("signature")
	private String signature = null;

	@JsonProperty("identificationMark")
	@Size(max = 300)
	private String identificationMark = null;

	@JsonProperty("photo")
	private String photo = null;
}
