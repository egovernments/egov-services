package org.egov.model;

import java.math.BigDecimal;
import java.util.List;

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
	  
	@JsonProperty("anticipatedLife")
	  private Long anticipatedLife = null;

	  @JsonProperty("orderNumber")
	  private String orderNumber = null;

	  @JsonProperty("orderDate")
	  private Long orderDate = null;

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

	  @JsonProperty("acquisitionDate")
	  private Long acquisitionDate = null;

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

	  @JsonProperty("usage")
	  private String usage = null;

	  @JsonProperty("locationDetails")
	  private Location locationDetails = null;

	  @JsonProperty("length")
	  private Double length = null;

	  @JsonProperty("width")
	  private Double width = null;

	  @JsonProperty("height")
	  private Double height = null;

	  @JsonProperty("totalArea")
	  private Double totalArea = null;

	  @JsonProperty("plinthArea")
	  private Double plinthArea = null;

	  @JsonProperty("address")
	  private String address = null;

	  @JsonProperty("longitude")
	  private Double longitude = null;

	  @JsonProperty("latitude")
	  private Double latitude = null;

	  @JsonProperty("floors")
	  private Long floors = null;

	  @JsonProperty("landSurveyNo")
	  private String landSurveyNo = null;

	  @JsonProperty("cubicContents")
	  private String cubicContents = null;

	  @JsonProperty("quantity")
	  private Long quantity = null;

	  @JsonProperty("tenantId")
	  private String tenantId = null;

	  @JsonProperty("id")
	  private Long id = null;

	  @JsonProperty("name")
	  private String name = null;

	  @JsonProperty("code")
	  private String code = null;

	  @JsonProperty("oldCode")
	  private String oldCode = null;

	  @JsonProperty("departmentCode")
	  private String departmentCode = null;

	  @JsonProperty("assetCategory")
	  private AssetCategory assetCategory = null;

	  @JsonProperty("modeOfAcquisition")
	  private ModeOfAcquisitionEnum modeOfAcquisition = null;

	  @JsonProperty("status")
	  private String status = null;

	  @JsonProperty("grossValue")
	  private BigDecimal grossValue = null;

	  @JsonProperty("accumulatedDepreciation")
	  private BigDecimal accumulatedDepreciation = null;

	  @JsonProperty("description")
	  private String description = null;

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


}