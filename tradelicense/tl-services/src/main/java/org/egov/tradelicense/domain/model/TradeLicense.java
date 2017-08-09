package org.egov.tradelicense.domain.model;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.tradelicense.domain.enums.ApplicationType;
import org.egov.tradelicense.domain.enums.BusinessNature;
import org.egov.tradelicense.domain.enums.OwnerShipType;
import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeLicense   {

  private Long id = null;

  @JsonProperty("tenantId")
  @NotNull
  @Size(min = 4, max = 128)
  private String tenantId = null;

  @NotNull
  private ApplicationType applicationType = null;

  @JsonProperty("applicationNumber")
  private String applicationNumber = null;

  @JsonProperty("licenseNumber")
  private String licenseNumber = null;

  @JsonProperty("oldLicenseNumber")
  private String oldLicenseNumber = null;

  @JsonProperty("applicationDate")
  private LocalDate applicationDate = null;

  @JsonProperty("adhaarNumber")
  private String adhaarNumber = null;
  
  @NotNull
  @JsonProperty("mobileNumber")
  private String mobileNumber = null;
 
  @NotNull
  @JsonProperty("ownerName")
  private String ownerName = null;

  @NotNull
  @JsonProperty("fatherSpouseName")
  private String fatherSpouseName = null;

  @NotNull
  @JsonProperty("emailId")
  private String emailId = null;

  @NotNull
  @JsonProperty("ownerAddress")
  private String ownerAddress = null;

  @JsonProperty("propertyAssesmentNo")
  private String propertyAssesmentNo = null;

  @NotNull
  @JsonProperty("localityId")
  private Integer localityId = null;

  @NotNull
  @JsonProperty("wardId")
  private Integer wardId = null;

  @NotNull
  @JsonProperty("tradeAddress")
  private String tradeAddress = null;

  @NotNull
  @JsonProperty("ownerShipType")
  private OwnerShipType ownerShipType = null;

  @NotNull
  @JsonProperty("tradeTitle")
  private String tradeTitle = null;

  @NotNull
  @JsonProperty("tradeType")
  private BusinessNature tradeType = null;

  
  @JsonProperty("categoryId")
  private Long categoryId = null;

  @JsonProperty("subCategoryId")
  private Long subCategoryId = null;

  @JsonProperty("uomId")
  private Long uomId = null;

  @JsonProperty("uomValue")
  private Double uomValue = null;

  @JsonProperty("remarks")
  private String remarks = null;

  @JsonProperty("tradeCommencementDate")
  private LocalDate tradeCommencementDate = null;

  @JsonProperty("agrementDate")
  private LocalDate agrementDate = null;

  @JsonProperty("agrementNo")
  private String agrementNo = null;

  @JsonProperty("isLegacy")
  private Boolean isLegacy = false;

  @JsonProperty("active")
  private Boolean active = true;

  @JsonProperty("expiryDate")
  private LocalDate expiryDate = null;

  @JsonProperty("feeDetails")
  private List<LicenseFeeDetail> feeDetails = null;

  @JsonProperty("supportDocuments")
  private List<SupportDocument> supportDocuments = null;

  @JsonProperty("auditDetails")
  private AuditDetails auditDetails = null;


}

