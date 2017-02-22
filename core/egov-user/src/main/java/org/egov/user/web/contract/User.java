package org.egov.user.web.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.egov.user.persistence.entity.enums.UserType;
import org.joda.time.LocalDate;

import java.util.Date;
import java.util.Set;

@Setter
@Getter
@Builder
public class User {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("salutation")
    private String salutation;

    @JsonProperty("name")
    private String name;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("mobileNumber")
    private String mobileNumber;

    @JsonProperty("emailId")
    private String emailId;

    @JsonProperty("altContactNumber")
    private String altContactNumber;

    @JsonProperty("pan")
    private String pan;

    @JsonProperty("aadhaarNumber")
    private String aadhaarNumber;

    @JsonProperty("permanentAddress")
    private String permanentAddress;

    @JsonProperty("permanentCity")
    private String permanentCity;

    @JsonProperty("permanentPinCode")
    private String permanentPinCode;

    @JsonProperty("correspondenceAddress")
    private String correspondenceAddress;

    @JsonProperty("correspondenceCity")
    private String correspondenceCity;

    @JsonProperty("correspondencePinCode")
    private String correspondencePinCode;

    @JsonProperty("active")
    private Boolean active;

    @JsonProperty("dob")
    @JsonFormat(pattern = "MM/dd/YYYY")
    private Date dob;

    @JsonProperty("pwdExpiryDate")
    @JsonFormat(pattern = "MM/dd/YYYY")
    private Date pwdExpiryDate;

    @JsonProperty("locale")
    private String locale;

    @JsonProperty("type")
    private UserType type;

    @JsonProperty("accountLocked")
    private Boolean accountLocked;

    @JsonProperty("roles")
    private Set<Role> roles;

    @JsonProperty("fatherOrHusbandName")
    private String fatherOrHusbandName;

    @JsonProperty("signature")
    private String signature;

    @JsonProperty("bloodGroup")
    private String bloodGroup;

    @JsonProperty("photo")
    private String photo;

    @JsonProperty("identificationMark")
    private String identificationMark;

    @JsonProperty("createdBy")
    private Long createdBy;

    @JsonProperty("createdDate")
    @JsonFormat(pattern = "MM/dd/YYYY")
    private Date createdDate;

    @JsonProperty("lastModifiedBy")
    private Long lastModifiedBy;

    @JsonProperty("lastModifiedDate")
    @JsonFormat(pattern = "MM/dd/YYYY")
    private Date lastModifiedDate;
}
