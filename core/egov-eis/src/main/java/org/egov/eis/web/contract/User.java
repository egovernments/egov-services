package org.egov.eis.web.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public class User {
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("username")
	private String username = null;

	@JsonProperty("password")
	private String password = null;

	@JsonProperty("salutation")
	private String salutation = null;

	@JsonProperty("name")
	private String name = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	public enum GenderEnum {
		MALE("MALE"),

		FEMALE("FEMALE"),

		OTHERS("OTHERS");

		private String value;

		GenderEnum(String value) {
			this.value = value;
		}

		@Override
		@JsonValue
		public String toString() {
			return String.valueOf(value);
		}

		@JsonCreator
		public static GenderEnum fromValue(String text) {
			for (GenderEnum b : GenderEnum.values()) {
				if (String.valueOf(b.value).equals(text)) {
					return b;
				}
			}
			return null;
		}
	}

	@JsonProperty("gender")
	private List<GenderEnum> gender = new ArrayList<GenderEnum>();

	@JsonProperty("mobileNumber")
	private String mobileNumber = null;

	@JsonProperty("emailId")
	private String emailId = null;

	@JsonProperty("altContactNumber")
	private String altContactNumber = null;

	@JsonProperty("pan")
	private String pan = null;

	@JsonProperty("aadhaarNumber")
	private String aadhaarNumber = null;

	@JsonProperty("address")
	private List<Address> address = new ArrayList<Address>();

	@JsonProperty("active")
	private Boolean active = null;

	@JsonProperty("dob")
	private LocalDate dob = null;

	@JsonProperty("pwdExpiryDate")
	private LocalDate pwdExpiryDate = null;

	@JsonProperty("locale")
	private String locale = null;

	public enum TypeEnum {
		EMPLOYEE("EMPLOYEE");

		private String value;

		TypeEnum(String value) {
			this.value = value;
		}

		@Override
		@JsonValue
		public String toString() {
			return String.valueOf(value);
		}

		@JsonCreator
		public static TypeEnum fromValue(String text) {
			for (TypeEnum b : TypeEnum.values()) {
				if (String.valueOf(b.value).equals(text)) {
					return b;
				}
			}
			return null;
		}
	}

	@JsonProperty("type")
	private List<TypeEnum> type = new ArrayList<TypeEnum>();

	@JsonProperty("signature")
	private byte[] signature = null;

	@JsonProperty("accountLocked")
	private Boolean accountLocked = null;

	@JsonProperty("roles")
	private List<Role> roles = new ArrayList<Role>();

	@JsonProperty("fatherOrHusbandName")
	private String fatherOrHusbandName = null;

	public enum BloodGroupEnum {
		A_PLUS("A+"),

		B_PLUS("B+"),

		O_PLUS("O+"),

		AB_PLUS("AB+"),

		A_MINUS("A-"),

		B_MINUS("B-"),

		AB_MINUS("AB-"),

		O_MINUS("O-");

		private String value;

		BloodGroupEnum(String value) {
			this.value = value;
		}

		@Override
		@JsonValue
		public String toString() {
			return String.valueOf(value);
		}

		@JsonCreator
		public static BloodGroupEnum fromValue(String text) {
			for (BloodGroupEnum b : BloodGroupEnum.values()) {
				if (String.valueOf(b.value).equals(text)) {
					return b;
				}
			}
			return null;
		}
	}

	@JsonProperty("bloodGroup")
	private List<BloodGroupEnum> bloodGroup = new ArrayList<BloodGroupEnum>();

	@JsonProperty("identificationMark")
	private String identificationMark = null;

	public User id(Long id) {
		this.id = id;
		return this;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User username(String username) {
		this.username = username;
		return this;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public User password(String password) {
		this.password = password;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User salutation(String salutation) {
		this.salutation = salutation;
		return this;
	}

	public String getSalutation() {
		return salutation;
	}

	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}

	public User name(String name) {
		this.name = name;
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User gender(List<GenderEnum> gender) {
		this.gender = gender;
		return this;
	}

	public User addGenderItem(GenderEnum genderItem) {
		this.gender.add(genderItem);
		return this;
	}

	public List<GenderEnum> getGender() {
		return gender;
	}

	public void setGender(List<GenderEnum> gender) {
		this.gender = gender;
	}

	public User mobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
		return this;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public User emailId(String emailId) {
		this.emailId = emailId;
		return this;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public User altContactNumber(String altContactNumber) {
		this.altContactNumber = altContactNumber;
		return this;
	}

	public String getAltContactNumber() {
		return altContactNumber;
	}

	public void setAltContactNumber(String altContactNumber) {
		this.altContactNumber = altContactNumber;
	}

	public User pan(String pan) {
		this.pan = pan;
		return this;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public User aadhaarNumber(String aadhaarNumber) {
		this.aadhaarNumber = aadhaarNumber;
		return this;
	}

	public String getAadhaarNumber() {
		return aadhaarNumber;
	}

	public void setAadhaarNumber(String aadhaarNumber) {
		this.aadhaarNumber = aadhaarNumber;
	}

	public User address(List<Address> address) {
		this.address = address;
		return this;
	}

	public User addAddressItem(Address addressItem) {
		this.address.add(addressItem);
		return this;
	}

	public List<Address> getAddress() {
		return address;
	}

	public void setAddress(List<Address> address) {
		this.address = address;
	}

	public User active(Boolean active) {
		this.active = active;
		return this;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public User dob(LocalDate dob) {
		this.dob = dob;
		return this;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public User pwdExpiryDate(LocalDate pwdExpiryDate) {
		this.pwdExpiryDate = pwdExpiryDate;
		return this;
	}

	public LocalDate getPwdExpiryDate() {
		return pwdExpiryDate;
	}

	public void setPwdExpiryDate(LocalDate pwdExpiryDate) {
		this.pwdExpiryDate = pwdExpiryDate;
	}

	public User locale(String locale) {
		this.locale = locale;
		return this;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public User type(List<TypeEnum> type) {
		this.type = type;
		return this;
	}

	public User addTypeItem(TypeEnum typeItem) {
		this.type.add(typeItem);
		return this;
	}

	public List<TypeEnum> getType() {
		return type;
	}

	public void setType(List<TypeEnum> type) {
		this.type = type;
	}

	public User signature(byte[] signature) {
		this.signature = signature;
		return this;
	}

	public byte[] getSignature() {
		return signature;
	}

	public void setSignature(byte[] signature) {
		this.signature = signature;
	}

	public User accountLocked(Boolean accountLocked) {
		this.accountLocked = accountLocked;
		return this;
	}

	public Boolean getAccountLocked() {
		return accountLocked;
	}

	public void setAccountLocked(Boolean accountLocked) {
		this.accountLocked = accountLocked;
	}

	public User roles(List<Role> roles) {
		this.roles = roles;
		return this;
	}

	public User addRolesItem(Role rolesItem) {
		this.roles.add(rolesItem);
		return this;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public User fatherOrHusbandName(String fatherOrHusbandName) {
		this.fatherOrHusbandName = fatherOrHusbandName;
		return this;
	}

	public String getFatherOrHusbandName() {
		return fatherOrHusbandName;
	}

	public void setFatherOrHusbandName(String fatherOrHusbandName) {
		this.fatherOrHusbandName = fatherOrHusbandName;
	}

	public User bloodGroup(List<BloodGroupEnum> bloodGroup) {
		this.bloodGroup = bloodGroup;
		return this;
	}

	public User addBloodGroupItem(BloodGroupEnum bloodGroupItem) {
		this.bloodGroup.add(bloodGroupItem);
		return this;
	}

	public List<BloodGroupEnum> getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(List<BloodGroupEnum> bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public User identificationMark(String identificationMark) {
		this.identificationMark = identificationMark;
		return this;
	}

	public String getIdentificationMark() {
		return identificationMark;
	}

	public void setIdentificationMark(String identificationMark) {
		this.identificationMark = identificationMark;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		User user = (User) o;
		return Objects.equals(this.id, user.id) && Objects.equals(this.username, user.username)
				&& Objects.equals(this.password, user.password) && Objects.equals(this.salutation, user.salutation)
				&& Objects.equals(this.name, user.name) && Objects.equals(this.gender, user.gender)
				&& Objects.equals(this.mobileNumber, user.mobileNumber) && Objects.equals(this.emailId, user.emailId)
				&& Objects.equals(this.altContactNumber, user.altContactNumber) && Objects.equals(this.pan, user.pan)
				&& Objects.equals(this.aadhaarNumber, user.aadhaarNumber) && Objects.equals(this.address, user.address)
				&& Objects.equals(this.active, user.active) && Objects.equals(this.dob, user.dob)
				&& Objects.equals(this.pwdExpiryDate, user.pwdExpiryDate) && Objects.equals(this.locale, user.locale)
				&& Objects.equals(this.type, user.type) && Objects.equals(this.signature, user.signature)
				&& Objects.equals(this.accountLocked, user.accountLocked) && Objects.equals(this.roles, user.roles)
				&& Objects.equals(this.fatherOrHusbandName, user.fatherOrHusbandName)
				&& Objects.equals(this.bloodGroup, user.bloodGroup) && Objects.equals(this.tenantId, user.tenantId)
				&& Objects.equals(this.identificationMark, user.identificationMark);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, username, password, salutation, name, gender, mobileNumber, emailId, altContactNumber,
				pan, aadhaarNumber, address, active, dob, pwdExpiryDate, locale, type, signature, accountLocked, roles,
				fatherOrHusbandName, bloodGroup, identificationMark, tenantId);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class User {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    username: ").append(toIndentedString(username)).append("\n");
		sb.append("    password: ").append(toIndentedString(password)).append("\n");
		sb.append("    salutation: ").append(toIndentedString(salutation)).append("\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    gender: ").append(toIndentedString(gender)).append("\n");
		sb.append("    mobileNumber: ").append(toIndentedString(mobileNumber)).append("\n");
		sb.append("    emailId: ").append(toIndentedString(emailId)).append("\n");
		sb.append("    altContactNumber: ").append(toIndentedString(altContactNumber)).append("\n");
		sb.append("    pan: ").append(toIndentedString(pan)).append("\n");
		sb.append("    aadhaarNumber: ").append(toIndentedString(aadhaarNumber)).append("\n");
		sb.append("    address: ").append(toIndentedString(address)).append("\n");
		sb.append("    active: ").append(toIndentedString(active)).append("\n");
		sb.append("    dob: ").append(toIndentedString(dob)).append("\n");
		sb.append("    pwdExpiryDate: ").append(toIndentedString(pwdExpiryDate)).append("\n");
		sb.append("    locale: ").append(toIndentedString(locale)).append("\n");
		sb.append("    type: ").append(toIndentedString(type)).append("\n");
		sb.append("    signature: ").append(toIndentedString(signature)).append("\n");
		sb.append("    accountLocked: ").append(toIndentedString(accountLocked)).append("\n");
		sb.append("    roles: ").append(toIndentedString(roles)).append("\n");
		sb.append("    fatherOrHusbandName: ").append(toIndentedString(fatherOrHusbandName)).append("\n");
		sb.append("    bloodGroup: ").append(toIndentedString(bloodGroup)).append("\n");
		sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
		sb.append("    identificationMark: ").append(toIndentedString(identificationMark)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
}
