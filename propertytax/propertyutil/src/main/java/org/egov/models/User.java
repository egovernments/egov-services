package org.egov.models;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * User
 * 
 * Author : Narendra
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {

	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("userName")
	@Size(min = 1, max = 64)
	private String userName = null;

	@JsonProperty("password")
	@Size(max = 64)
	private String password = null;

	@JsonProperty("salutation")
	@Size(max = 5)
	private String salutation = null;

	@JsonProperty("name")
	@NotNull
	@Size(min = 1, max = 100)
	private String name = null;

	@JsonProperty("gender")
	@NotNull
	private String gender = null;

	@JsonProperty("mobileNumber")
	@Size(min = 10, max = 10)
	private String mobileNumber = null;

	@JsonProperty("emailId")
	@Size(max = 128)
	private String emailId = null;

	@JsonProperty("altContactNumber")
	@Size(max = 10)
	private String altContactNumber = null;

	@JsonProperty("pan")
	@Size(max = 10)
	private String pan = null;

	@JsonProperty("aadhaarNumber")
	@Pattern(regexp = "[0-9]{12}")
	@Size(min = 12, max = 12)
	private String aadhaarNumber = null;

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

	@JsonProperty("active")
	@NotNull
	private Boolean active = null;

	@JsonProperty("dob")
	private String dob = null;

	@JsonProperty("pwdExpiryDate")
	private String pwdExpiryDate = null;

	@JsonProperty("locale")
	@NotNull
	@Size(max = 10)
	private String locale = null;

	@JsonProperty("type")
	@NotNull
	@Size(max = 20)
	private String type = null;

	@JsonProperty("signature")
	private String signature = null;

	@JsonProperty("accountLocked")
	private Boolean accountLocked = false;

	@JsonProperty("roles")
	@Valid
	private List<Role> roles = new ArrayList<Role>();

	@JsonProperty("fatherOrHusbandName")
	@Size(max = 100)
	private String fatherOrHusbandName = null;

	@JsonProperty("bloodGroup")
	@Size(max = 3)
	private String bloodGroup = null;

	@JsonProperty("identificationMark")
	@Size(max = 300)
	private String identificationMark = null;

	@JsonProperty("photo")
	private String photo = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

	@JsonProperty("otpReference")
	private String otpReference = null;

	@JsonProperty("tenantId")
	@NotNull
	private String tenantId = null;

	@JsonProperty("isPrimaryOwner")
	private Boolean isPrimaryOwner = null;

	@JsonProperty("isSecondaryOwner")
	private Boolean isSecondaryOwner = null;

	@JsonProperty("ownerShipPercentage")
	private Double ownerShipPercentage = null;

	@JsonProperty("ownerType")
	@Size(min = 4, max = 32)
	private String ownerType = null;

	@JsonProperty("owner")
	private Long owner = null;

}
