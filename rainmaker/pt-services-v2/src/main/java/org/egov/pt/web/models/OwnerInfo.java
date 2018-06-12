package org.egov.pt.web.models;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * OwnerInfo
 */
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2018-05-11T14:12:44.497+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of= {"id"})
public class OwnerInfo extends User  {

        @JsonProperty("isPrimaryOwner")
        private Boolean isPrimaryOwner;

        @JsonProperty("ownerShipPercentage")
        private Double ownerShipPercentage;

        @JsonProperty("ownerType")
        private String ownerType;


        @Builder
        public OwnerInfo(Long id, String uuid, String userName, String password, String salutation, String name, String gender, String mobileNumber, String emailId, String altContactNumber, String pan, String aadhaarNumber, String permanentAddress, String permanentCity, String permanentPincode, String correspondenceCity, String correspondencePincode, String correspondenceAddress, Boolean active, Long dob, Long pwdExpiryDate, String locale, String type, String signature, Boolean accountLocked, List<Role> roles, String fatherOrHusbandName, String bloodGroup, String identificationMark, String photo, String createdBy, Long createdDate, Long lastModifiedBy, Long lastModifiedDate, String otpReference, String tenantId, Boolean isPrimaryOwner, Double ownerShipPercentage, String ownerType) {
                super(id,uuid, userName, password, salutation, name, gender, mobileNumber, emailId, altContactNumber, pan, aadhaarNumber, permanentAddress, permanentCity, permanentPincode, correspondenceCity, correspondencePincode, correspondenceAddress, active, dob, pwdExpiryDate, locale, type, signature, accountLocked, roles, fatherOrHusbandName, bloodGroup, identificationMark, photo, createdBy, createdDate, lastModifiedBy, lastModifiedDate, otpReference, tenantId);
                this.isPrimaryOwner = isPrimaryOwner;
                this.ownerShipPercentage = ownerShipPercentage;
                this.ownerType = ownerType;
        }



}

