package org.egov.pt.web.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * User
 */
@Validated

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User   {
        @JsonProperty("id")
        private Long id;

        @JsonProperty("uuid")
        private String uuid;

        @JsonProperty("userName")
        private String userName;

        @JsonProperty("password")
        private String password;

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

        @JsonProperty("permanentPincode")
        private String permanentPincode;

        @JsonProperty("correspondenceCity")
        private String correspondenceCity;

        @JsonProperty("correspondencePincode")
        private String correspondencePincode;

        @JsonProperty("correspondenceAddress")
        private String correspondenceAddress;

        @JsonProperty("active")
        private Boolean active;

        @JsonProperty("dob")
        private Long dob;

        @JsonProperty("pwdExpiryDate")
        private Long pwdExpiryDate;

        @JsonProperty("locale")
        private String locale;

        @JsonProperty("type")
        private String type;

        @JsonProperty("signature")
        private String signature;

        @JsonProperty("accountLocked")
        private Boolean accountLocked;

        @JsonProperty("roles")
        @Valid
        private List<Role> roles;

        @JsonProperty("fatherOrHusbandName")
        private String fatherOrHusbandName;

        @JsonProperty("bloodGroup")
        private String bloodGroup;

        @JsonProperty("identificationMark")
        private String identificationMark;

        @JsonProperty("photo")
        private String photo;

        @JsonProperty("createdBy")
        private String createdBy;

        @JsonProperty("createdDate")
        private Long createdDate;

        @JsonProperty("lastModifiedBy")
        private Long lastModifiedBy;

        @JsonProperty("lastModifiedDate")
        private Long lastModifiedDate;

        @JsonProperty("otpReference")
        private String otpReference;

        @JsonProperty("tenantId")
        private String tenantId;


        public User addRolesItem(Role rolesItem) {
            if (this.roles == null) {
            this.roles = new ArrayList<>();
            }
        this.roles.add(rolesItem);
        return this;
        }

}

