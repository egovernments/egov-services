package org.egov.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * User
 * 
 * Author : Narendra
 */
public class User   {
	@JsonProperty("tenantId")
	@NotNull
	private String tenantId = null;

	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("userName")
	private String userName = null;

	@JsonProperty("authToken")
	private String authToken = null;

	@JsonProperty("salutation")
	@Size(max=5)
	private String salutation = null;

	@JsonProperty("name")
	@NotNull
	@Size(min=3,max=100)
	private String name = null;

	@JsonProperty("gender")
	@NotNull
	private String gender = null;

	@JsonProperty("mobileNumber")
	@NotNull
	@Size(max=10)
	private String mobileNumber = null;

	@JsonProperty("emailId")
	@Size(max=128)
	private String emailId = null;

	@JsonProperty("aadhaarNumber")
	@Pattern(regexp="[0-9]")
	@Size(max=12)
	private String aadhaarNumber = null;

	@JsonProperty("active")
	@NotNull
	private Boolean active = null;

	@JsonProperty("pwdExpiryDate")
	private Long pwdExpiryDate = null;

	@JsonProperty("locale")
	@NotNull
	@Size(max=5)
	private String locale = null;

	@JsonProperty("type")
	@NotNull
	@Size(max=20)
	private String type = null;

	@JsonProperty("accountLocked")
	private Boolean accountLocked = false;

	@JsonProperty("roles")
	@Valid
	private List<Role> roles = new ArrayList<Role>();

	@JsonProperty("userDetails")
	@Valid
	private UserDetails userDetails = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

	/**
	 * Unique Identifier of the tenant to which user primarily belongs
	 * @return tenantId
	 **/
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	/**
	 * The unique numeric id of an user. To be deprecated in future.
	 * @return id
	 **/
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * The unique username used of the user - this will be used for user login. This needs to be unique within the tenant.
	 * @return userName
	 **/
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Authentication token for the user that may be -  1. Password in case of basic auth 2. OTP in case of OTP based verification 3. OAuth auth token for OAuth based auth 
	 * @return authToken
	 **/
	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	/**
	 * The salutation of user name. Example- Mr, Miss, Mrs
	 * @return salutation
	 **/

	public String getSalutation() {
		return salutation;
	}

	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}

	/**
	 * The full name of the user.
	 * @return name
	 **/
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gender of the user.
	 * @return gender
	 **/
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * Mobile number of the user
	 * @return mobileNumber
	 **/
	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	/**
	 * Email address of the user
	 * @return emailId
	 **/
	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	/**
	 * Aadhaar number of the user
	 * @return aadhaarNumber
	 **/
	public String getAadhaarNumber() {
		return aadhaarNumber;
	}

	public void setAadhaarNumber(String aadhaarNumber) {
		this.aadhaarNumber = aadhaarNumber;
	}


	/**
	 * True if the user is active and False if the user is inactive.
	 * @return active
	 **/
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	/**
	 * epoch of time when the current auth token (password/OTP) will expire.
	 * @return pwdExpiryDate
	 **/

	public Long getPwdExpiryDate() {
		return pwdExpiryDate;
	}

	public void setPwdExpiryDate(Long pwdExpiryDate) {
		this.pwdExpiryDate = pwdExpiryDate;
	}

	/**
	 * Value will be set to \"en_IN\".
	 * @return locale
	 **/
	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	/**
	 * System set value internally. For employee value will be always \"EMPLOYEE\". For citizen value will be \"CITIZEN\".
	 * @return type
	 **/
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Set to True if account is locked after several incorrect password attempt. False if account is not locked.
	 * @return accountLocked
	 **/
	public Boolean getAccountLocked() {
		return accountLocked;
	}

	public void setAccountLocked(Boolean accountLocked) {
		this.accountLocked = accountLocked;
	}


	/**
	 * List of roles that are attached to the user.
	 * @return roles
	 **/
	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	/**
	 * Get userDetails
	 * @return userDetails
	 **/
	public UserDetails getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}


	/**
	 * Get auditDetails
	 * @return auditDetails
	 **/
	public AuditDetails getAuditDetails() {
		return auditDetails;
	}

	public void setAuditDetails(AuditDetails auditDetails) {
		this.auditDetails = auditDetails;
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
		return Objects.equals(this.tenantId, user.tenantId) &&
				Objects.equals(this.id, user.id) &&
				Objects.equals(this.userName, user.userName) &&
				Objects.equals(this.authToken, user.authToken) &&
				Objects.equals(this.salutation, user.salutation) &&
				Objects.equals(this.name, user.name) &&
				Objects.equals(this.gender, user.gender) &&
				Objects.equals(this.mobileNumber, user.mobileNumber) &&
				Objects.equals(this.emailId, user.emailId) &&
				Objects.equals(this.aadhaarNumber, user.aadhaarNumber) &&
				Objects.equals(this.active, user.active) &&
				Objects.equals(this.pwdExpiryDate, user.pwdExpiryDate) &&
				Objects.equals(this.locale, user.locale) &&
				Objects.equals(this.type, user.type) &&
				Objects.equals(this.accountLocked, user.accountLocked) &&
				Objects.equals(this.roles, user.roles) &&
				Objects.equals(this.userDetails, user.userDetails) &&
				Objects.equals(this.auditDetails, user.auditDetails);
	}

	@Override
	public int hashCode() {
		return Objects.hash(tenantId, id, userName, authToken, salutation, name, gender, mobileNumber, emailId, aadhaarNumber, active, pwdExpiryDate, locale, type, accountLocked, roles, userDetails, auditDetails);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class User {\n");

		sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    userName: ").append(toIndentedString(userName)).append("\n");
		sb.append("    authToken: ").append(toIndentedString(authToken)).append("\n");
		sb.append("    salutation: ").append(toIndentedString(salutation)).append("\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    gender: ").append(toIndentedString(gender)).append("\n");
		sb.append("    mobileNumber: ").append(toIndentedString(mobileNumber)).append("\n");
		sb.append("    emailId: ").append(toIndentedString(emailId)).append("\n");
		sb.append("    aadhaarNumber: ").append(toIndentedString(aadhaarNumber)).append("\n");
		sb.append("    active: ").append(toIndentedString(active)).append("\n");
		sb.append("    pwdExpiryDate: ").append(toIndentedString(pwdExpiryDate)).append("\n");
		sb.append("    locale: ").append(toIndentedString(locale)).append("\n");
		sb.append("    type: ").append(toIndentedString(type)).append("\n");
		sb.append("    accountLocked: ").append(toIndentedString(accountLocked)).append("\n");
		sb.append("    roles: ").append(toIndentedString(roles)).append("\n");
		sb.append("    userDetails: ").append(toIndentedString(userDetails)).append("\n");
		sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
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

