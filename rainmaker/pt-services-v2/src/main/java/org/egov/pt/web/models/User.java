package org.egov.pt.web.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * User
 */
@Validated

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

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

        @NotNull
        @Pattern(regexp = "[a-z-A-Z' ']*", message = "Name has invalid characters")
        @JsonProperty("name")
        private String name;

        @JsonProperty("gender")
        private String gender;

        @NotNull
        @Pattern(regexp = "[0-9]*", message = "MobileNumber has invalid Number")
        @JsonProperty("mobileNumber")
        private String mobileNumber;

        @JsonProperty("emailId")
        private String emailId;

        @JsonProperty("altContactNumber")
        private String altContactNumber;

        @JsonProperty("pan")
        private String pan;

        @Digits(integer = 12,fraction=0)
        @JsonProperty("aadhaarNumber")
        private String aadhaarNumber;

        @JsonProperty("permanentAddress")
        private String permanentAddress;

        @JsonProperty("permanentCity")
        private String permanentCity;

        @JsonProperty("permanentPinCode")
        private String permanentPincode;

        @JsonProperty("correspondenceCity")
        private String correspondenceCity;

        @JsonProperty("correspondencePinCode")
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

        @NotNull
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
        private String lastModifiedBy;

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

        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                User user = (User) o;
                return Objects.equals(uuid, user.uuid) &&
                        Objects.equals(name, user.name) &&
                        Objects.equals(mobileNumber, user.mobileNumber);
        }

        @Override
        public int hashCode() {

                return Objects.hash(uuid, name, mobileNumber);
        }
}

