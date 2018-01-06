package org.egov.swm.domain.model;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Supplier {

    @Length(min = 0, max = 256, message = "Value of tenantId shall be between 1 and 128")
    @JsonProperty("tenantId")
    private String tenantId = null;

    @Length(min = 0, max = 128, message = "Value of supplierNo shall be between 1 and 128")
    @JsonProperty("supplierNo")
    private String supplierNo = null;

    private String supplierNos = null;

    @Length(min = 0, max = 100, message = "Value of name shall be between 1 and 100")
    @JsonProperty("name")
    private String name = null;

    @Length(min = 0, max = 100, message = "Value of agencyName shall be between 1 and 100")
    @JsonProperty("agencyName")
    private String agencyName = null;

    @NotNull
    @Length(min = 7, max = 256, message = "Value of email shall be between 7 and 256")
    @JsonProperty("email")
    private String email = null;

    @Length(min = 0, max = 256, message = "Value of tinNumber shall be between 1 and 256")
    @JsonProperty("tinNumber")
    private String tinNumber = null;

    @NotNull
    @Length(min = 1, max = 256, message = "Value of gst shall be between 1 and 256")
    @JsonProperty("gst")
    private String gst = null;

    @Length(min = 0, max = 10, message = "Please enter a ten digit mobile number")
    @JsonProperty("mobileNo")
    private String mobileNo = null;

    @NotNull
    @Length(min = 10, max = 10, message = "Please enter a ten digit contact number")
    @JsonProperty("contactNo")
    private String contactNo = null;

    @Length(min = 0, max = 10, message = "Please enter a ten digit fax number")
    @JsonProperty("faxNumber")
    private String faxNumber = null;

    @NotNull
    @Length(min = 10, max = 500, message = "Value of address shall be between 10 and 500")
    @JsonProperty("address")
    private String address = null;

    @Length(min = 0, max = 256, message = "Value of registrationNo shall be between 1 and 256")
    @JsonProperty("registrationNo")
    private String registrationNo = null;

    @Length(min = 0, max = 12, message = "Please enter twelve digit aadhar number")
    @JsonProperty("aadharNo")
    private String aadharNo = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;
}
