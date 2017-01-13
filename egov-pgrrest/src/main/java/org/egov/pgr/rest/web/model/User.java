package org.egov.pgr.rest.web.model;

import java.util.Objects;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public class User {

    public enum Gender {
        FEMALE("FEMALE"),

        MALE("MALE"),

        OTHERS("OTHERS");

        private String value;

        Gender(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static Gender fromValue(String text) {
            for (Gender b : Gender.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    public enum Type {
        CITIZEN("CITIZEN"),

        EMPLOYEE("EMPLOYEE"),

        SYSTEM("SYSTEM");

        private String value;

        Type(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static Type fromValue(String text) {
            for (Type b : Type.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    public enum Status {
        EMPLOYED("EMPLOYED"),

        RETIRED("RETIRED"),

        SUSPENDED("SUSPENDED"),

        DECEASED("DECEASED");

        private String value;

        Status(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static Status fromValue(String text) {
            for (Status b : Status.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("user_name")
    private String userName = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("mobile_no")
    private String mobileNo = null;

    @JsonProperty("email")
    private String email = null;

    @JsonProperty("password")
    private String password = null;

    @JsonProperty("new_password")
    private String newPassword = null;

    @JsonProperty("confirm_password")
    private String confirmPassword = null;

    @JsonProperty("salutation")
    private String salutation = null;

    @JsonProperty("guardian")
    private String guardian = null;

    @JsonProperty("guardian_relation")
    private String guardianRelation = null;

    @JsonProperty("gender")
    private Gender gender = null;

    @JsonProperty("alt_contact_number")
    private String altContactNumber = null;

    @JsonProperty("pan")
    private String pan = null;

    @JsonProperty("aadhaar_number")
    private String aadhaarNumber = null;

    @JsonProperty("dob")
    private LocalDate dob = null;

    @JsonProperty("pwd_expiry_date")
    private LocalDate pwdExpiryDate = null;

    @JsonProperty("type")
    private Type type = null;

    @JsonProperty("account_locked")
    private Boolean accountLocked = null;

    @JsonProperty("code")
    private String code = null;

    @JsonProperty("date_of_appointment")
    private LocalDate dateOfAppointment = null;

    @JsonProperty("date_of_retirement")
    private LocalDate dateOfRetirement = null;

    @JsonProperty("status")
    private Status status = null;

    @JsonProperty("device_id")
    private String deviceId = null;

    @JsonProperty("device_type")
    private String deviceType = null;

    @JsonProperty("os_version")
    private String osVersion = null;

    public User userName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public User mobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
        return this;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public User email(String email) {
        this.email = email;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public User newPassword(String newPassword) {
        this.newPassword = newPassword;
        return this;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public User confirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        return this;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
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

    public User guardian(String guardian) {
        this.guardian = guardian;
        return this;
    }

    public String getGuardian() {
        return guardian;
    }

    public void setGuardian(String guardian) {
        this.guardian = guardian;
    }

    public User guardianRelation(String guardianRelation) {
        this.guardianRelation = guardianRelation;
        return this;
    }

    public String getGuardianRelation() {
        return guardianRelation;
    }

    public void setGuardianRelation(String guardianRelation) {
        this.guardianRelation = guardianRelation;
    }

    public User gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
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

    public User type(Type type) {
        this.type = type;
        return this;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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

    public User code(String code) {
        this.code = code;
        return this;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public User dateOfAppointment(LocalDate dateOfAppointment) {
        this.dateOfAppointment = dateOfAppointment;
        return this;
    }

    public LocalDate getDateOfAppointment() {
        return dateOfAppointment;
    }

    public void setDateOfAppointment(LocalDate dateOfAppointment) {
        this.dateOfAppointment = dateOfAppointment;
    }

    public User dateOfRetirement(LocalDate dateOfRetirement) {
        this.dateOfRetirement = dateOfRetirement;
        return this;
    }

    public LocalDate getDateOfRetirement() {
        return dateOfRetirement;
    }

    public void setDateOfRetirement(LocalDate dateOfRetirement) {
        this.dateOfRetirement = dateOfRetirement;
    }

    public User status(Status status) {
        this.status = status;
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User deviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public User deviceType(String deviceType) {
        this.deviceType = deviceType;
        return this;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public User osVersion(String osVersion) {
        this.osVersion = osVersion;
        return this;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
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
        return Objects.equals(this.userName, user.userName) &&
                Objects.equals(this.name, user.name) &&
                Objects.equals(this.mobileNo, user.mobileNo) &&
                Objects.equals(this.email, user.email) &&
                Objects.equals(this.password, user.password) &&
                Objects.equals(this.newPassword, user.newPassword) &&
                Objects.equals(this.confirmPassword, user.confirmPassword) &&
                Objects.equals(this.salutation, user.salutation) &&
                Objects.equals(this.guardian, user.guardian) &&
                Objects.equals(this.guardianRelation, user.guardianRelation) &&
                Objects.equals(this.gender, user.gender) &&
                Objects.equals(this.altContactNumber, user.altContactNumber) &&
                Objects.equals(this.pan, user.pan) &&
                Objects.equals(this.aadhaarNumber, user.aadhaarNumber) &&
                Objects.equals(this.dob, user.dob) &&
                Objects.equals(this.pwdExpiryDate, user.pwdExpiryDate) &&
                Objects.equals(this.type, user.type) &&
                Objects.equals(this.accountLocked, user.accountLocked) &&
                Objects.equals(this.code, user.code) &&
                Objects.equals(this.dateOfAppointment, user.dateOfAppointment) &&
                Objects.equals(this.dateOfRetirement, user.dateOfRetirement) &&
                Objects.equals(this.status, user.status) &&
                Objects.equals(this.deviceId, user.deviceId) &&
                Objects.equals(this.deviceType, user.deviceType) &&
                Objects.equals(this.osVersion, user.osVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, name, mobileNo, email, password, newPassword, confirmPassword, salutation, guardian,
                guardianRelation, gender, altContactNumber, pan, aadhaarNumber, dob, pwdExpiryDate, type, accountLocked, code,
                dateOfAppointment, dateOfRetirement, status, deviceId, deviceType, osVersion);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class User {\n");

        sb.append("    userName: ").append(toIndentedString(userName)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    mobileNo: ").append(toIndentedString(mobileNo)).append("\n");
        sb.append("    email: ").append(toIndentedString(email)).append("\n");
        sb.append("    password: ").append(toIndentedString(password)).append("\n");
        sb.append("    newPassword: ").append(toIndentedString(newPassword)).append("\n");
        sb.append("    confirmPassword: ").append(toIndentedString(confirmPassword)).append("\n");
        sb.append("    salutation: ").append(toIndentedString(salutation)).append("\n");
        sb.append("    guardian: ").append(toIndentedString(guardian)).append("\n");
        sb.append("    guardianRelation: ").append(toIndentedString(guardianRelation)).append("\n");
        sb.append("    gender: ").append(toIndentedString(gender)).append("\n");
        sb.append("    altContactNumber: ").append(toIndentedString(altContactNumber)).append("\n");
        sb.append("    pan: ").append(toIndentedString(pan)).append("\n");
        sb.append("    aadhaarNumber: ").append(toIndentedString(aadhaarNumber)).append("\n");
        sb.append("    dob: ").append(toIndentedString(dob)).append("\n");
        sb.append("    pwdExpiryDate: ").append(toIndentedString(pwdExpiryDate)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    accountLocked: ").append(toIndentedString(accountLocked)).append("\n");
        sb.append("    code: ").append(toIndentedString(code)).append("\n");
        sb.append("    dateOfAppointment: ").append(toIndentedString(dateOfAppointment)).append("\n");
        sb.append("    dateOfRetirement: ").append(toIndentedString(dateOfRetirement)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    deviceId: ").append(toIndentedString(deviceId)).append("\n");
        sb.append("    deviceType: ").append(toIndentedString(deviceType)).append("\n");
        sb.append("    osVersion: ").append(toIndentedString(osVersion)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
