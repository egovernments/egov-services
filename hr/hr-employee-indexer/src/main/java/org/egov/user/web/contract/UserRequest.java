package org.egov.user.web.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRequest {

    private Long id;

    private String userName;

    private String salutation;

    private String name;

    private String gender;

    private String mobileNumber;

    private String emailId;

    private String altContactNumber;

    private String pan;

    private String aadhaarNumber;

    private String permanentAddress;

    private String permanentCity;

    private String permanentPinCode;

    private String correspondenceAddress;

    private String correspondenceCity;

    private String correspondencePinCode;

    private Boolean active;

    private String locale;

    private String type;

    private Boolean accountLocked;

    private String fatherOrHusbandName;

    private String signature;

    private String bloodGroup;

    private String photo;

    private String identificationMark;

    private Long createdBy;

    private String password;

    private String otpReference;

    private Long lastModifiedBy;

    private String tenantId;

    private List<RoleRequest> roles;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
    private Date createdDate;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
    private Date lastModifiedDate;

    @JsonFormat(pattern = "YYYY-MM-dd")
    private Date dob;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
    private Date pwdExpiryDate;

}