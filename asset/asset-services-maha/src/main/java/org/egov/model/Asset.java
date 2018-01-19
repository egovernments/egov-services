package org.egov.model;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
public class Asset {

	@JsonProperty("anticipatedLife")
	private Long anticipatedLife;

	@JsonProperty("orderNumber")
	private String orderNumber;

	@JsonProperty("orderDate")
	private Long orderDate;

	@JsonProperty("wipReferenceNo")
	private String wipReferenceNo;

	@JsonProperty("acquiredFrom")
	private String acquiredFrom;

	@JsonProperty("warrantyAvailable")
	private Boolean warrantyAvailable;

	@JsonProperty("warrantyExpiryDate")
	private Long warrantyExpiryDate;

	@JsonProperty("defectLiabilityPeriod")
	private DefectLiability defectLiabilityPeriod;

	@JsonProperty("securityDepositRetained")
	private BigDecimal securityDepositRetained;

	@JsonProperty("securityDepositRealized")
	private BigDecimal securityDepositRealized;

	@NotNull
	@JsonProperty("acquisitionDate")
	private Long acquisitionDate;

	@NotNull
	@JsonProperty("originalValue")
	private BigDecimal originalValue;

	@JsonProperty("assetAccount")
	private String assetAccount;

	@JsonProperty("accumulatedDepreciationAccount")
	private String accumulatedDepreciationAccount;

	@JsonProperty("revaluationReserveAccount")
	private String revaluationReserveAccount;

	@JsonProperty("depreciationExpenseAccount")
	private String depreciationExpenseAccount;

	@JsonProperty("titleDocumentsAvailable")
	private List<String> titleDocumentsAvailable;

	@JsonProperty("locationDetails")
	private Location locationDetails;

	@JsonProperty("totalArea")
	private Double totalArea;

	@NotNull
	@JsonProperty("address")
	private String address;

	@JsonProperty("longitude")
	private Double longitude;

	@JsonProperty("latitude")
	private Double latitude;

	@JsonProperty("quantity")
	private Long quantity;

	@JsonProperty("tenantId")
	private String tenantId;

	@JsonProperty("id")
	private Long id;

	@NotNull
	@JsonProperty("name")
	private String name;

	@JsonProperty("code")
	private String code;

	@JsonProperty("oldCode")
	private String oldCode;

	@NotNull
	@JsonProperty("department")
	private Department department;

	@NotNull
	@Valid
	@JsonProperty("assetCategory")
	private AssetCategory assetCategory;

	@NotNull
	@JsonProperty("modeOfAcquisition")
	@Valid
	private ModeOfAcquisition modeOfAcquisition;

	@JsonProperty("status")
	private String status;

	@NotNull
	@JsonProperty("grossValue")
	private BigDecimal grossValue;

	@JsonProperty("accumulatedDepreciation")
	private BigDecimal accumulatedDepreciation;

	@NotNull
	@JsonProperty("description")
	private String description;

	@NotNull
	@JsonProperty("dateOfCreation")
	private Long dateOfCreation;

	@JsonProperty("remarks")
	private String remarks;

	@JsonProperty("version")
	private String version;

	@JsonProperty("assetReference")
	private Long assetReference;

	@JsonProperty("enableYearWiseDepreciation")
	private Boolean enableYearWiseDepreciation;

	@JsonProperty("assetAttributes")
	private List<Attributes> assetAttributes;

	@JsonProperty("depreciationRate")
	private Double depreciationRate;

	@JsonProperty("yearWiseDepreciationRate")
	private List<YearWiseDepreciation> yearWiseDepreciationRate;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;

	@JsonProperty("fundSource")
	@Valid
	private FundSource fundSource;

	@JsonProperty("location")
	private String location;

	@NotNull
	@JsonProperty("openingDate")
	private Long openingDate;

	@JsonProperty("landDetails")
	@Valid
	private List<LandDetail> landDetails;

	@JsonProperty("currentValue")
	private BigDecimal currentValue;
	
	@JsonProperty("transactionHistory")
	private List<TransactionHistory> transactionHistory;
	
	@JsonProperty("unitOfMeasurement")
	private String unitOfMeasurement;
	
	
}