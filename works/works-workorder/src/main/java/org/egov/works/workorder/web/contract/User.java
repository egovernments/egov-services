package org.egov.works.workorder.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.joda.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * User
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-11-30T11:45:24.744Z")

public class User {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("userName")
    private String userName = null;

    @JsonProperty("password")
    private String password = null;

    @JsonProperty("salutation")
    private String salutation = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("gender")
    private String gender = null;

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

    @JsonProperty("permanentAddress")
    private String permanentAddress = null;

    @JsonProperty("permanentCity")
    private String permanentCity = null;

    @JsonProperty("permanentPincode")
    private String permanentPincode = null;

    @JsonProperty("correspondenceCity")
    private String correspondenceCity = null;

    @JsonProperty("correspondencePincode")
    private String correspondencePincode = null;

    @JsonProperty("correspondenceAddress")
    private String correspondenceAddress = null;

    @JsonProperty("active")
    private Boolean active = null;

    @JsonProperty("dob")
    private LocalDate dob = null;

    @JsonProperty("pwdExpiryDate")
    private LocalDate pwdExpiryDate = null;

    @JsonProperty("locale")
    private String locale = null;

    @JsonProperty("type")
    private String type = null;

    @JsonProperty("signature")
    private String signature = null;

    @JsonProperty("accountLocked")
    private Boolean accountLocked = null;

    @JsonProperty("roles")
    private List<Role> roles = null;

    @JsonProperty("fatherOrHusbandName")
    private String fatherOrHusbandName = null;

    @JsonProperty("bloodGroup")
    private String bloodGroup = null;

    @JsonProperty("identificationMark")
    private String identificationMark = null;

    @JsonProperty("photo")
    private String photo = null;

    @JsonProperty("createdBy")
    private Long createdBy = null;

    @JsonProperty("createdDate")
    private LocalDate createdDate = null;

    @JsonProperty("lastModifiedBy")
    private Long lastModifiedBy = null;

    @JsonProperty("lastModifiedDate")
    private LocalDate lastModifiedDate = null;

    @JsonProperty("otpReference")
    private String otpReference = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    public User id(Long id) {
        this.id = id;
        return this;
    }

    /**
     * The unique id an user.
     *
     * @return id
     **/
    @ApiModelProperty(value = "The unique id an user.")


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User userName(String userName) {
        this.userName = userName;
        return this;
    }

    /**
     * The unique username used for user login.
     *
     * @return userName
     **/
    @ApiModelProperty(required = true, value = "The unique username used for user login.")
    @NotNull

    @Size(min = 1, max = 64)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public User password(String password) {
        this.password = password;
        return this;
    }

    /**
     * Password for user login.
     *
     * @return password
     **/
    @ApiModelProperty(value = "Password for user login.")

    @Size(max = 64)
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

    /**
     * The salutation of user name. Example- Mr, Miss, Mrs
     *
     * @return salutation
     **/
    @ApiModelProperty(value = "The salutation of user name. Example- Mr, Miss, Mrs")

    @Size(max = 5)
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

    /**
     * The full name of the user.
     *
     * @return name
     **/
    @ApiModelProperty(required = true, value = "The full name of the user.")
    @NotNull

    @Size(min = 3, max = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User gender(String gender) {
        this.gender = gender;
        return this;
    }

    /**
     * Gender of the user.
     *
     * @return gender
     **/
    @ApiModelProperty(required = true, value = "Gender of the user.")
    @NotNull


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public User mobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
        return this;
    }

    /**
     * Mobile number ofuser the user
     *
     * @return mobileNumber
     **/
    @ApiModelProperty(required = true, value = "Mobile number ofuser the user")
    @NotNull

    @Size(max = 10)
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

    /**
     * Email address of the user
     *
     * @return emailId
     **/
    @ApiModelProperty(value = "Email address of the user")

    @Size(max = 128)
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

    /**
     * Alternate contact number of the user
     *
     * @return altContactNumber
     **/
    @ApiModelProperty(value = "Alternate contact number of the user")

    @Size(max = 10)
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

    /**
     * PAN number of the user
     *
     * @return pan
     **/
    @ApiModelProperty(value = "PAN number of the user")

    @Size(max = 10)
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

    /**
     * Aadhaar number of the user
     *
     * @return aadhaarNumber
     **/
    @ApiModelProperty(value = "Aadhaar number of the user")

    @Pattern(regexp = "[0-9]")
    @Size(max = 12)
    public String getAadhaarNumber() {
        return aadhaarNumber;
    }

    public void setAadhaarNumber(String aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }

    public User permanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
        return this;
    }

    /**
     * Permanent address of the user.
     *
     * @return permanentAddress
     **/
    @ApiModelProperty(value = "Permanent address of the user.")

    @Size(max = 300)
    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public User permanentCity(String permanentCity) {
        this.permanentCity = permanentCity;
        return this;
    }

    /**
     * City of the permanent address.
     *
     * @return permanentCity
     **/
    @ApiModelProperty(value = "City of the permanent address.")

    @Size(max = 300)
    public String getPermanentCity() {
        return permanentCity;
    }

    public void setPermanentCity(String permanentCity) {
        this.permanentCity = permanentCity;
    }

    public User permanentPincode(String permanentPincode) {
        this.permanentPincode = permanentPincode;
        return this;
    }

    /**
     * Permanent address pincode.
     *
     * @return permanentPincode
     **/
    @ApiModelProperty(value = "Permanent address pincode.")

    @Size(max = 6)
    public String getPermanentPincode() {
        return permanentPincode;
    }

    public void setPermanentPincode(String permanentPincode) {
        this.permanentPincode = permanentPincode;
    }

    public User correspondenceCity(String correspondenceCity) {
        this.correspondenceCity = correspondenceCity;
        return this;
    }

    /**
     * City of the correspondence address.
     *
     * @return correspondenceCity
     **/
    @ApiModelProperty(value = "City of the correspondence address.")

    @Size(max = 50)
    public String getCorrespondenceCity() {
        return correspondenceCity;
    }

    public void setCorrespondenceCity(String correspondenceCity) {
        this.correspondenceCity = correspondenceCity;
    }

    public User correspondencePincode(String correspondencePincode) {
        this.correspondencePincode = correspondencePincode;
        return this;
    }

    /**
     * Permanent address pincode.
     *
     * @return correspondencePincode
     **/
    @ApiModelProperty(value = "Permanent address pincode.")

    @Size(max = 6)
    public String getCorrespondencePincode() {
        return correspondencePincode;
    }

    public void setCorrespondencePincode(String correspondencePincode) {
        this.correspondencePincode = correspondencePincode;
    }

    public User correspondenceAddress(String correspondenceAddress) {
        this.correspondenceAddress = correspondenceAddress;
        return this;
    }

    /**
     * Correspondence address of the user.
     *
     * @return correspondenceAddress
     **/
    @ApiModelProperty(value = "Correspondence address of the user.")

    @Size(max = 300)
    public String getCorrespondenceAddress() {
        return correspondenceAddress;
    }

    public void setCorrespondenceAddress(String correspondenceAddress) {
        this.correspondenceAddress = correspondenceAddress;
    }

    public User active(Boolean active) {
        this.active = active;
        return this;
    }

    /**
     * True if the user is active and False if the user is inactive.
     *
     * @return active
     **/
    @ApiModelProperty(required = true, value = "True if the user is active and False if the user is inactive.")
    @NotNull


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

    /**
     * Data of bith of the user in dd/mm/yyyy format.
     *
     * @return dob
     **/
    @ApiModelProperty(value = "Data of bith of the user in dd/mm/yyyy format.")

    @Valid

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

    /**
     * Date on which the password registered will expire.
     *
     * @return pwdExpiryDate
     **/
    @ApiModelProperty(readOnly = true, value = "Date on which the password registered will expire.")

    @Valid

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

    /**
     * Value will be set to \"en_IN\".
     *
     * @return locale
     **/
    @ApiModelProperty(required = true, value = "Value will be set to \"en_IN\".")
    @NotNull

    @Size(max = 10)
    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public User type(String type) {
        this.type = type;
        return this;
    }

    /**
     * System set value internally. For employee value will be always \"EMPLOYEE\". For citizen value will be \"CITIZEN\".
     *
     * @return type
     **/
    @ApiModelProperty(required = true, value = "System set value internally. For employee value will be always \"EMPLOYEE\". For citizen value will be \"CITIZEN\".")
    @NotNull

    @Size(max = 20)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User signature(String signature) {
        this.signature = signature;
        return this;
    }

    /**
     * Image to be loaded for the signature of the employee
     *
     * @return signature
     **/
    @ApiModelProperty(value = "Image to be loaded for the signature of the employee")


    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public User accountLocked(Boolean accountLocked) {
        this.accountLocked = accountLocked;
        return this;
    }

    /**
     * Set to True if account is locked after several incorrect password attempt. False if account is not locked.
     *
     * @return accountLocked
     **/
    @ApiModelProperty(value = "Set to True if account is locked after several incorrect password attempt. False if account is not locked.")


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
        if (this.roles == null) {
            this.roles = new ArrayList<Role>();
        }
        this.roles.add(rolesItem);
        return this;
    }

    /**
     * List of roles that are attached to the user.
     *
     * @return roles
     **/
    @ApiModelProperty(value = "List of roles that are attached to the user.")

    @Valid

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

    /**
     * Name of user's father or husband.
     *
     * @return fatherOrHusbandName
     **/
    @ApiModelProperty(value = "Name of user's father or husband.")

    @Size(max = 100)
    public String getFatherOrHusbandName() {
        return fatherOrHusbandName;
    }

    public void setFatherOrHusbandName(String fatherOrHusbandName) {
        this.fatherOrHusbandName = fatherOrHusbandName;
    }

    public User bloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
        return this;
    }

    /**
     * Blood group of the user.
     *
     * @return bloodGroup
     **/
    @ApiModelProperty(value = "Blood group of the user.")

    @Size(max = 3)
    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public User identificationMark(String identificationMark) {
        this.identificationMark = identificationMark;
        return this;
    }

    /**
     * Any identification mark of the person.
     *
     * @return identificationMark
     **/
    @ApiModelProperty(value = "Any identification mark of the person.")

    @Size(max = 300)
    public String getIdentificationMark() {
        return identificationMark;
    }

    public void setIdentificationMark(String identificationMark) {
        this.identificationMark = identificationMark;
    }

    public User photo(String photo) {
        this.photo = photo;
        return this;
    }

    /**
     * Image to be loaded for the photo of the user
     *
     * @return photo
     **/
    @ApiModelProperty(value = "Image to be loaded for the photo of the user")


    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public User createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    /**
     * Id of the user who created the record.
     *
     * @return createdBy
     **/
    @ApiModelProperty(value = "Id of the user who created the record.")


    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public User createdDate(LocalDate createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    /**
     * Date on which the user master data was added into the system.
     *
     * @return createdDate
     **/
    @ApiModelProperty(value = "Date on which the user master data was added into the system.")

    @Valid

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public User lastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    /**
     * Id of the user who last modified the record.
     *
     * @return lastModifiedBy
     **/
    @ApiModelProperty(value = "Id of the user who last modified the record.")


    public Long getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public User lastModifiedDate(LocalDate lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    /**
     * Date on which the user master data was last modified.
     *
     * @return lastModifiedDate
     **/
    @ApiModelProperty(value = "Date on which the user master data was last modified.")

    @Valid

    public LocalDate getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDate lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public User otpReference(String otpReference) {
        this.otpReference = otpReference;
        return this;
    }

    /**
     * This is the UUID token that we genarate as part of OTP.
     *
     * @return otpReference
     **/
    @ApiModelProperty(value = "This is the UUID token that we genarate as part of OTP.")


    public String getOtpReference() {
        return otpReference;
    }

    public void setOtpReference(String otpReference) {
        this.otpReference = otpReference;
    }

    public User tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * Unique Identifier of the tenant, Like AP, AP.Kurnool etc.
     *
     * @return tenantId
     **/
    @ApiModelProperty(required = true, value = "Unique Identifier of the tenant, Like AP, AP.Kurnool etc.")
    @NotNull


    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(this.id, user.id) &&
                Objects.equals(this.userName, user.userName) &&
                Objects.equals(this.password, user.password) &&
                Objects.equals(this.salutation, user.salutation) &&
                Objects.equals(this.name, user.name) &&
                Objects.equals(this.gender, user.gender) &&
                Objects.equals(this.mobileNumber, user.mobileNumber) &&
                Objects.equals(this.emailId, user.emailId) &&
                Objects.equals(this.altContactNumber, user.altContactNumber) &&
                Objects.equals(this.pan, user.pan) &&
                Objects.equals(this.aadhaarNumber, user.aadhaarNumber) &&
                Objects.equals(this.permanentAddress, user.permanentAddress) &&
                Objects.equals(this.permanentCity, user.permanentCity) &&
                Objects.equals(this.permanentPincode, user.permanentPincode) &&
                Objects.equals(this.correspondenceCity, user.correspondenceCity) &&
                Objects.equals(this.correspondencePincode, user.correspondencePincode) &&
                Objects.equals(this.correspondenceAddress, user.correspondenceAddress) &&
                Objects.equals(this.active, user.active) &&
                Objects.equals(this.dob, user.dob) &&
                Objects.equals(this.pwdExpiryDate, user.pwdExpiryDate) &&
                Objects.equals(this.locale, user.locale) &&
                Objects.equals(this.type, user.type) &&
                Objects.equals(this.signature, user.signature) &&
                Objects.equals(this.accountLocked, user.accountLocked) &&
                Objects.equals(this.roles, user.roles) &&
                Objects.equals(this.fatherOrHusbandName, user.fatherOrHusbandName) &&
                Objects.equals(this.bloodGroup, user.bloodGroup) &&
                Objects.equals(this.identificationMark, user.identificationMark) &&
                Objects.equals(this.photo, user.photo) &&
                Objects.equals(this.createdBy, user.createdBy) &&
                Objects.equals(this.createdDate, user.createdDate) &&
                Objects.equals(this.lastModifiedBy, user.lastModifiedBy) &&
                Objects.equals(this.lastModifiedDate, user.lastModifiedDate) &&
                Objects.equals(this.otpReference, user.otpReference) &&
                Objects.equals(this.tenantId, user.tenantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, password, salutation, name, gender, mobileNumber, emailId, altContactNumber, pan, aadhaarNumber, permanentAddress, permanentCity, permanentPincode, correspondenceCity, correspondencePincode, correspondenceAddress, active, dob, pwdExpiryDate, locale, type, signature, accountLocked, roles, fatherOrHusbandName, bloodGroup, identificationMark, photo, createdBy, createdDate, lastModifiedBy, lastModifiedDate, otpReference, tenantId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class User {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    userName: ").append(toIndentedString(userName)).append("\n");
        sb.append("    password: ").append(toIndentedString(password)).append("\n");
        sb.append("    salutation: ").append(toIndentedString(salutation)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    gender: ").append(toIndentedString(gender)).append("\n");
        sb.append("    mobileNumber: ").append(toIndentedString(mobileNumber)).append("\n");
        sb.append("    emailId: ").append(toIndentedString(emailId)).append("\n");
        sb.append("    altContactNumber: ").append(toIndentedString(altContactNumber)).append("\n");
        sb.append("    pan: ").append(toIndentedString(pan)).append("\n");
        sb.append("    aadhaarNumber: ").append(toIndentedString(aadhaarNumber)).append("\n");
        sb.append("    permanentAddress: ").append(toIndentedString(permanentAddress)).append("\n");
        sb.append("    permanentCity: ").append(toIndentedString(permanentCity)).append("\n");
        sb.append("    permanentPincode: ").append(toIndentedString(permanentPincode)).append("\n");
        sb.append("    correspondenceCity: ").append(toIndentedString(correspondenceCity)).append("\n");
        sb.append("    correspondencePincode: ").append(toIndentedString(correspondencePincode)).append("\n");
        sb.append("    correspondenceAddress: ").append(toIndentedString(correspondenceAddress)).append("\n");
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
        sb.append("    identificationMark: ").append(toIndentedString(identificationMark)).append("\n");
        sb.append("    photo: ").append(toIndentedString(photo)).append("\n");
        sb.append("    createdBy: ").append(toIndentedString(createdBy)).append("\n");
        sb.append("    createdDate: ").append(toIndentedString(createdDate)).append("\n");
        sb.append("    lastModifiedBy: ").append(toIndentedString(lastModifiedBy)).append("\n");
        sb.append("    lastModifiedDate: ").append(toIndentedString(lastModifiedDate)).append("\n");
        sb.append("    otpReference: ").append(toIndentedString(otpReference)).append("\n");
        sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

