package org.egov.tlcalculator.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.egov.tlcalculator.web.models.Role;
import org.egov.tlcalculator.web.models.User;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

/**
 * OwnerInfo
 */
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2018-09-27T14:56:03.454+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OwnerInfo   {
        @JsonProperty("id")
        
private Long id = null;

        @JsonProperty("userName")
        @NotNull@Size(min=1,max=64) 
private String userName = null;

        @JsonProperty("password")
        @Size(max=64) 
private String password = null;

        @JsonProperty("salutation")
        @Size(max=5) 
private String salutation = null;

        @JsonProperty("name")
        @NotNull@Size(min=3,max=100) 
private String name = null;

        @JsonProperty("gender")
        @NotNull
private String gender = null;

        @JsonProperty("mobileNumber")
        @NotNull@Size(max=10) 
private String mobileNumber = null;

        @JsonProperty("emailId")
        @Size(max=128) 
private String emailId = null;

        @JsonProperty("altContactNumber")
        @Size(max=10) 
private String altContactNumber = null;

        @JsonProperty("pan")
        @Size(max=10) 
private String pan = null;

        @JsonProperty("aadhaarNumber")
        @Pattern(regexp="[0-9]") @Size(max=12) 
private String aadhaarNumber = null;

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

        @JsonProperty("active")
        @NotNull
private Boolean active = null;

        @JsonProperty("dob")
        @Valid
private LocalDate dob = null;

        @JsonProperty("pwdExpiryDate")
        @Valid
private LocalDate pwdExpiryDate = null;

        @JsonProperty("locale")
        @NotNull@Size(max=10) 
private String locale = null;

        @JsonProperty("type")
        @NotNull@Size(max=20) 
private String type = null;

        @JsonProperty("signature")
        
private String signature = null;

        @JsonProperty("accountLocked")
        
private Boolean accountLocked = null;

        @JsonProperty("roles")
        @Valid
        private List<Role> roles = null;

        @JsonProperty("fatherOrHusbandName")
        @Size(max=100) 
private String fatherOrHusbandName = null;

        @JsonProperty("bloodGroup")
        @Size(max=3) 
private String bloodGroup = null;

        @JsonProperty("identificationMark")
        @Size(max=300) 
private String identificationMark = null;

        @JsonProperty("photo")
        
private String photo = null;

        @JsonProperty("createdBy")
        
private Long createdBy = null;

        @JsonProperty("createdDate")
        @Valid
private LocalDate createdDate = null;

        @JsonProperty("lastModifiedBy")
        
private Long lastModifiedBy = null;

        @JsonProperty("lastModifiedDate")
        @Valid
private LocalDate lastModifiedDate = null;

        @JsonProperty("otpReference")
        
private String otpReference = null;

        @JsonProperty("tenantId")
        @NotNull
private String tenantId = null;

        @JsonProperty("isPrimaryOwner")
        
private Boolean isPrimaryOwner = null;

        @JsonProperty("ownerShipPercentage")
        
private Double ownerShipPercentage = null;

        @JsonProperty("ownerType")
        @Size(min=4,max=256) 
private String ownerType = null;

              /**
   * Relationship with owner.
   */
  public enum RelationshipEnum {
    FATHER("FATHER"),
    
    HUSBAND("HUSBAND");

    private String value;

    RelationshipEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static RelationshipEnum fromValue(String text) {
      for (RelationshipEnum b : RelationshipEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

        @JsonProperty("relationship")
        
private RelationshipEnum relationship = null;


}

