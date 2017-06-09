package org.egov.models;

import java.util.Objects;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Infrequently used details for the user
 * Author : Narendra
 */
public class UserDetails   {
	@JsonProperty("firstName")
	@Size(min=1,max=32)
	private String firstName = null;

	@JsonProperty("middleName")
	@Size(min=1,max=32)
	private String middleName = null;

	@JsonProperty("lastName")
	@Size(min=1,max=32)
	private String lastName = null;

	@JsonProperty("dob")
	private String dob = null;

	@JsonProperty("altContactNumber")
	@Size(max=16)
	private String altContactNumber = null;

	@JsonProperty("fatherName")
	@Size(max=100)
	private String fatherName = null;

	@JsonProperty("husbandName")
	@Size(max=100)
	private String husbandName = null;

	@JsonProperty("bloodGroup")
	@Size(max=3)
	private String bloodGroup = null;

	@JsonProperty("pan")
	@Size(max=10)
	private String pan = null;

	@JsonProperty("permanentAddress")
	@Size(max=300)
	private String permanentAddress = null;

	@JsonProperty("permanentCity")
	@Size(max=300)
	private String permanentCity = null;

	@JsonProperty("permanentPincode")
	@Size(max=6)
	private String permanentPincode = null;

	@JsonProperty("correspondenceCity")
	@Size(max=50)
	private String correspondenceCity = null;

	@JsonProperty("correspondencePincode")
	@Size(max=6)
	private String correspondencePincode = null;

	@JsonProperty("correspondenceAddress")
	@Size(max=300)
	private String correspondenceAddress = null;

	@JsonProperty("signature")
	private String signature = null;

	@JsonProperty("identificationMark")
	@Size(max=300)
	private String identificationMark = null;

	@JsonProperty("photo")
	private String photo = null;

	public UserDetails firstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	/**
	 * The full name of the user.
	 * @return firstName
	 **/
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public UserDetails middleName(String middleName) {
		this.middleName = middleName;
		return this;
	}

	/**
	 * The full name of the user.
	 * @return middleName
	 **/
	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public UserDetails lastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	/**
	 * The full name of the user.
	 * @return lastName
	 **/
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public UserDetails dob(String dob) {
		this.dob = dob;
		return this;
	}

	/**
	 * Date of birth of the user in dd/mm/yyyy format.
	 * @return dob
	 **/
	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public UserDetails altContactNumber(String altContactNumber) {
		this.altContactNumber = altContactNumber;
		return this;
	}

	/**
	 * Alternate contact number of the user
	 * @return altContactNumber
	 **/
	public String getAltContactNumber() {
		return altContactNumber;
	}

	public void setAltContactNumber(String altContactNumber) {
		this.altContactNumber = altContactNumber;
	}

	public UserDetails fatherName(String fatherName) {
		this.fatherName = fatherName;
		return this;
	}

	/**
	 * User's father's name if available
	 * @return fatherName
	 **/
	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public UserDetails husbandName(String husbandName) {
		this.husbandName = husbandName;
		return this;
	}

	/**
	 * Name of user's husband - if applicable
	 * @return husbandName
	 **/
	public String getHusbandName() {
		return husbandName;
	}

	public void setHusbandName(String husbandName) {
		this.husbandName = husbandName;
	}

	public UserDetails bloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
		return this;
	}

	/**
	 * Blood group of the user.
	 * @return bloodGroup
	 **/
	public String getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public UserDetails pan(String pan) {
		this.pan = pan;
		return this;
	}

	/**
	 * PAN number of the user
	 * @return pan
	 **/
	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public UserDetails permanentAddress(String permanentAddress) {
		this.permanentAddress = permanentAddress;
		return this;
	}

	/**
	 * Permanent address of the user.
	 * @return permanentAddress
	 **/
	public String getPermanentAddress() {
		return permanentAddress;
	}

	public void setPermanentAddress(String permanentAddress) {
		this.permanentAddress = permanentAddress;
	}

	public UserDetails permanentCity(String permanentCity) {
		this.permanentCity = permanentCity;
		return this;
	}

	/**
	 * City of the permanent address.
	 * @return permanentCity
	 **/
	public String getPermanentCity() {
		return permanentCity;
	}

	public void setPermanentCity(String permanentCity) {
		this.permanentCity = permanentCity;
	}

	public UserDetails permanentPincode(String permanentPincode) {
		this.permanentPincode = permanentPincode;
		return this;
	}

	/**
	 * Permanent address pincode.
	 * @return permanentPincode
	 **/
	public String getPermanentPincode() {
		return permanentPincode;
	}

	public void setPermanentPincode(String permanentPincode) {
		this.permanentPincode = permanentPincode;
	}

	public UserDetails correspondenceCity(String correspondenceCity) {
		this.correspondenceCity = correspondenceCity;
		return this;
	}

	/**
	 * City of the correspondence address.
	 * @return correspondenceCity
	 **/
	public String getCorrespondenceCity() {
		return correspondenceCity;
	}

	public void setCorrespondenceCity(String correspondenceCity) {
		this.correspondenceCity = correspondenceCity;
	}

	public UserDetails correspondencePincode(String correspondencePincode) {
		this.correspondencePincode = correspondencePincode;
		return this;
	}

	/**
	 * Permanent address pincode.
	 * @return correspondencePincode
	 **/
	public String getCorrespondencePincode() {
		return correspondencePincode;
	}

	public void setCorrespondencePincode(String correspondencePincode) {
		this.correspondencePincode = correspondencePincode;
	}

	public UserDetails correspondenceAddress(String correspondenceAddress) {
		this.correspondenceAddress = correspondenceAddress;
		return this;
	}

	/**
	 * Correspondence address of the user.
	 * @return correspondenceAddress
	 **/
	public String getCorrespondenceAddress() {
		return correspondenceAddress;
	}

	public void setCorrespondenceAddress(String correspondenceAddress) {
		this.correspondenceAddress = correspondenceAddress;
	}

	public UserDetails signature(String signature) {
		this.signature = signature;
		return this;
	}

	/**
	 * Image to be loaded for the signature of the employee
	 * @return signature
	 **/
	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public UserDetails identificationMark(String identificationMark) {
		this.identificationMark = identificationMark;
		return this;
	}

	/**
	 * Any identification mark of the person.
	 * @return identificationMark
	 **/

	public String getIdentificationMark() {
		return identificationMark;
	}

	public void setIdentificationMark(String identificationMark) {
		this.identificationMark = identificationMark;
	}

	public UserDetails photo(String photo) {
		this.photo = photo;
		return this;
	}

	/**
	 * Image to be loaded for the photo of the user
	 * @return photo
	 **/

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}


	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		UserDetails userDetails = (UserDetails) o;
		return Objects.equals(this.firstName, userDetails.firstName) &&
				Objects.equals(this.middleName, userDetails.middleName) &&
				Objects.equals(this.lastName, userDetails.lastName) &&
				Objects.equals(this.dob, userDetails.dob) &&
				Objects.equals(this.altContactNumber, userDetails.altContactNumber) &&
				Objects.equals(this.fatherName, userDetails.fatherName) &&
				Objects.equals(this.husbandName, userDetails.husbandName) &&
				Objects.equals(this.bloodGroup, userDetails.bloodGroup) &&
				Objects.equals(this.pan, userDetails.pan) &&
				Objects.equals(this.permanentAddress, userDetails.permanentAddress) &&
				Objects.equals(this.permanentCity, userDetails.permanentCity) &&
				Objects.equals(this.permanentPincode, userDetails.permanentPincode) &&
				Objects.equals(this.correspondenceCity, userDetails.correspondenceCity) &&
				Objects.equals(this.correspondencePincode, userDetails.correspondencePincode) &&
				Objects.equals(this.correspondenceAddress, userDetails.correspondenceAddress) &&
				Objects.equals(this.signature, userDetails.signature) &&
				Objects.equals(this.identificationMark, userDetails.identificationMark) &&
				Objects.equals(this.photo, userDetails.photo);
	}

	@Override
	public int hashCode() {
		return Objects.hash(firstName, middleName, lastName, dob, altContactNumber, fatherName, husbandName, bloodGroup, pan, permanentAddress, permanentCity, permanentPincode, correspondenceCity, correspondencePincode, correspondenceAddress, signature, identificationMark, photo);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class UserDetails {\n");

		sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
		sb.append("    middleName: ").append(toIndentedString(middleName)).append("\n");
		sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
		sb.append("    dob: ").append(toIndentedString(dob)).append("\n");
		sb.append("    altContactNumber: ").append(toIndentedString(altContactNumber)).append("\n");
		sb.append("    fatherName: ").append(toIndentedString(fatherName)).append("\n");
		sb.append("    husbandName: ").append(toIndentedString(husbandName)).append("\n");
		sb.append("    bloodGroup: ").append(toIndentedString(bloodGroup)).append("\n");
		sb.append("    pan: ").append(toIndentedString(pan)).append("\n");
		sb.append("    permanentAddress: ").append(toIndentedString(permanentAddress)).append("\n");
		sb.append("    permanentCity: ").append(toIndentedString(permanentCity)).append("\n");
		sb.append("    permanentPincode: ").append(toIndentedString(permanentPincode)).append("\n");
		sb.append("    correspondenceCity: ").append(toIndentedString(correspondenceCity)).append("\n");
		sb.append("    correspondencePincode: ").append(toIndentedString(correspondencePincode)).append("\n");
		sb.append("    correspondenceAddress: ").append(toIndentedString(correspondenceAddress)).append("\n");
		sb.append("    signature: ").append(toIndentedString(signature)).append("\n");
		sb.append("    identificationMark: ").append(toIndentedString(identificationMark)).append("\n");
		sb.append("    photo: ").append(toIndentedString(photo)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}

