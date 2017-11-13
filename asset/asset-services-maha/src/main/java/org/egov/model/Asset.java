package org.egov.model;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.model.enums.ModeOfAcquisitionEnum;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
public class Asset   {

	  @NotNull
	  @JsonProperty("anticipatedLife")
	  private Long anticipatedLife = null;

	  @JsonProperty("orderNumber")
	  private String orderNumber = null;

	  @JsonProperty("orderDate")
	  private Long orderDate = null;

	  @NotNull
	  @JsonProperty("wipReferenceNo")
	  private String wipReferenceNo = null;

	  @JsonProperty("acquiredFrom")
	  private String acquiredFrom = null;

	  @JsonProperty("warrantyAvailable")
	  private Boolean warrantyAvailable = null;

	  @JsonProperty("warrantyExpiryDate")
	  private Long warrantyExpiryDate = null;

	  @JsonProperty("defectLiabilityPeriod")
	  private DefectLiability defectLiabilityPeriod = null;

	  @JsonProperty("securityDepositRetained")
	  private BigDecimal securityDepositRetained = null;

	  @JsonProperty("securityDepositRealized")
	  private BigDecimal securityDepositRealized = null;

	  @NotNull
	  @JsonProperty("acquisitionDate")
	  private Long acquisitionDate = null;

	  @NotNull
	  @JsonProperty("originalValue")
	  private BigDecimal originalValue = null;

	  @JsonProperty("assetAccount")
	  private String assetAccount = null;
	  
	  @JsonProperty("accumulatedDepreciationAccount")
	  private String accumulatedDepreciationAccount = null;

	  @JsonProperty("revaluationReserveAccount")
	  private String revaluationReserveAccount = null;
	  
	  @JsonProperty("depreciationExpenseAccount")
	  private String depreciationExpenseAccount = null;

	  @JsonProperty("titleDocumentsAvalable")
	  private List<String> titleDocumentsAvalable = null;

	  @JsonProperty("locationDetails")
	  private Location locationDetails = null;

	  @JsonProperty("totalArea")
	  private Double totalArea = null;

	  @NotNull
	  @JsonProperty("address")
	  private String address = null;

	  @JsonProperty("longitude")
	  private Double longitude = null;

	  @JsonProperty("latitude")
	  private Double latitude = null;

	  @JsonProperty("landSurveyNo")
	  private String landSurveyNo = null;

	  @JsonProperty("quantity")
	  private Long quantity = null;

	  @JsonProperty("tenantId")
	  private String tenantId = null;

	  @JsonProperty("id")
	  private Long id = null;

	  @NotNull
	  @JsonProperty("name")
	  private String name = null;

	  @JsonProperty("code")
	  private String code = null;

	  @JsonProperty("oldCode")
	  private String oldCode = null;

	  @NotNull
	  @JsonProperty("departmentCode")
	  private String departmentCode = null;

	  @NotNull
	  @Valid
	  @JsonProperty("assetCategory")
	  private AssetCategory assetCategory = null;

	  @NotNull
	  @JsonProperty("modeOfAcquisition")
	  private ModeOfAcquisitionEnum modeOfAcquisition = null;

	  @JsonProperty("status")
	  private String status = null;

	  @NotNull
	  @JsonProperty("grossValue")
	  private BigDecimal grossValue = null;

	  @JsonProperty("accumulatedDepreciation")
	  private BigDecimal accumulatedDepreciation = null;

	  @NotNull
	  @JsonProperty("description")
	  private String description = null;

	  @NotNull
	  @JsonProperty("dateOfCreation")
	  private Long dateOfCreation = null;

	  @JsonProperty("remarks")
	  private String remarks = null;

	  @JsonProperty("version")
	  private String version = null;

	  @JsonProperty("assetReference")
	  private Long assetReference = null;

	  @JsonProperty("enableYearWiseDepreciation")
	  private Boolean enableYearWiseDepreciation = null;

	  @JsonProperty("assetAttributes")
	  private List<Attributes> assetAttributes = null;

	  @JsonProperty("depreciationRate")
	  private Double depreciationRate = null;

	  @JsonProperty("yearWiseDepreciationRate")
	  private List<YearWiseDepreciation> yearWiseDepreciationRate = null;
	  
	  @JsonProperty("auditDetails")
	  private AuditDetails auditDetails;
	  
	  @JsonProperty("fundSource")
	  private String fundSource;
	  
	  @JsonProperty("location")
	  private String location;
	  
	  @JsonProperty("openingDate")
	  private Long openingDate;

	  @JsonProperty("landDetails")
	  private List<LandDetail> landDetails;
	  
	  @JsonProperty("currentValue")
	  private BigDecimal currentValue;
}