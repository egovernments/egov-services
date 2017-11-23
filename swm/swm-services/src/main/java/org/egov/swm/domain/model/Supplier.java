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

    @Length(min = 0, max = 256)
    @JsonProperty("tenantId")
    private String tenantId = null;

    @Length(min = 0, max = 128)
    @JsonProperty("supplierNo")
    private String supplierNo = null;
    
    private String supplierNos = null;

    @Length(min = 0, max = 100)
    @JsonProperty("name")
    private String name = null;

    @Length(min = 0, max = 100)
    @JsonProperty("agencyName")
    private String agencyName = null;

    @NotNull
    @Length(min = 7, max = 256)
    @JsonProperty("email")
    private String email = null;

    @NotNull
    @Length(min = 1, max = 256)
    @JsonProperty("tinNumber")
    private String tinNumber = null;

    @NotNull
    @Length(min = 1, max = 256)
    @JsonProperty("gst")
    private String gst = null;

    @Length(min = 10, max = 10)
    @JsonProperty("mobileNo")
    private String mobileNo = null;

    @NotNull
    @Length(min = 10, max = 10)
    @JsonProperty("contactNo")
    private String contactNo = null;

    @Length(min = 0, max = 10)
    @JsonProperty("faxNumber")
    private String faxNumber = null;

    @NotNull
    @Length(min = 10, max = 500)
    @JsonProperty("address")
    private String address = null;

    @Length(min = 0, max = 256)
    @JsonProperty("registrationNo")
    private String registrationNo = null;

    @Length(min = 0, max = 12)
    @JsonProperty("aadharNo")
    private String aadharNo = null;

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;
}
