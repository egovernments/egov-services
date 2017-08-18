package org.egov.tl.commons.web.contract;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.egov.tl.commons.web.contract.enums.ApplicationTypeEnum;
import org.egov.tl.commons.web.contract.enums.BusinessNatureEnum;
import org.egov.tl.commons.web.contract.enums.OwnerShipTypeEnum;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TradeLicenseContract {

	private Long id;

	@JsonProperty("tenantId")
	@NotEmpty(message="tenantID is required, Please enter valid tenant id with length between 4- 128")
	@Length(min = 4, max = 128, message="tenantID is required, Please enter valid tenant id with length between 4- 128")
	private String tenantId;

	@NotNull(message="Application Type is required, Please enter valid Application Type")
	private ApplicationTypeEnum applicationType;

	@JsonProperty("applicationNumber")
	private String applicationNumber;

	@JsonProperty("licenseNumber")
	private String licenseNumber;

	@JsonProperty("oldLicenseNumber") 
	@Length( min =4, max =20, message="oldLicenseNumber is required, Please enter valid Old License Number between  4-20 characters")
	private String oldLicenseNumber;

	@NotNull(message="applicationDate is required, Please enter valid date in epoc")
	@JsonProperty("applicationDate")
	private Long applicationDate;

	@JsonProperty("adhaarNumber")
	@Pattern(regexp = "[0-9]{12}", message = "Aadhaar Number is required , please enter 12 digits Aadhaar Number")
	@Length(min = 12, max = 12, message = "Aadhaar Number is required , please enter 12 digits Aadhaar Number")
	private String adhaarNumber;

	@NotEmpty(message = "Mobile Nubmer is required , please enter 10 digits Mobile Number")
	@JsonProperty("mobileNumber")
	@Length(min = 10, max = 10, message = "Mobile Nubmer is required , please enter 10 digits Mobile Number")
	@Pattern(regexp = "[0-9]{10}",  message = "Mobile Nubmer is required , please enter 10 digits Mobile Number")
	private String mobileNumber;

	@NotEmpty(message = "Owner Name is required , please enter Valid name length between 4-100 characters")
	@Length(min = 4, max = 100, message = "Owner Name is required , please enter Valid name length between 4-100 characters")
	@JsonProperty("ownerName")
	private String ownerName;

	@NotEmpty(message = "Father Spouse Name is required ,  please enter Valid name length between 4-100 characters")
	@Length(min = 4, max = 100, message = "Father Spouse Name is required ,  please enter Valid name length between 4-100 characters")
	@JsonProperty("fatherSpouseName")
	private String fatherSpouseName;

	@NotEmpty(message="Emailid is required. Please enter the valid emailid")
	@Length( min = 1, max = 50, message="Emailid more than 50 characters not allowed.")
	@Email(message="Emailid is required. Please enter the valid emailid")
	@JsonProperty("emailId")
	private String emailId;

	@NotEmpty(message = "Owner Address  is required ,  please enter Valid Address length between 4-250 characters")
	@Length( min = 4, max = 250, message = "Owner Address  is required ,  please enter Valid Address length between 4-250 characters")
	@JsonProperty("ownerAddress")
	private String ownerAddress;

	@JsonProperty("propertyAssesmentNo")
	@Length( min = 4, max = 20, message = "Property Assesment No  is required ,  please enter Valid Property Assesment No  between 4-20 characters")
	private String propertyAssesmentNo;

	@NotNull(message = "Locality  is required ,  please enter valid Locality ")
	@JsonProperty("localityId")
	private Integer localityId;

	@NotNull(message = "RevenueWard  is required ,  please enter valid RevenueWard ")
	@JsonProperty("revenueWardId")
	private Integer revenueWardId;

	@NotNull(message = "Admin Ward  is required ,  please enter valid Admin Ward ")
	@JsonProperty("adminWardId")
	private Integer adminWardId;

	@NotEmpty(message = "Trade Address  is required ,  please enter Valid Address length between 4-250 characters")
	@JsonProperty("tradeAddress")
	@Length( min = 4, max = 250)
	private String tradeAddress;

	@NotNull(message="OwnerShip Type is required, Please enter valid OwnerShip Type")
	@JsonProperty("ownerShipType")
	private OwnerShipTypeEnum ownerShipType;

	@NotEmpty(message = "Trade Title  is required ,  please enter Valid Name length between 4-100 characters")
	@JsonProperty("tradeTitle")
	@Length( min =4, max = 100, message = "Trade Title  is required ,  please enter Valid Name length between 4-100 characters")
	private String tradeTitle;

	@NotNull(message = "Trade Type  is required ,  please enter Valid Trade Type")
	@JsonProperty("tradeType")
	private BusinessNatureEnum tradeType;

	@NotNull(message = "Category is required ,  please enter Valid Category")
	@JsonProperty("categoryId")
	private Long categoryId;

	@NotNull(message = "Sub Category is required ,  please enter Valid SubCategory")
	@JsonProperty("subCategoryId")
	private Long subCategoryId;

	@NotNull(message = "Uom is required ,  please enter Valid Uom")
	@JsonProperty("uomId")
	private Long uomId;

	@NotNull(message = "Quantity is required ,  please enter Valid Quantity")
	@Digits(integer=10, fraction=2,message="Quantity should not have more than 2 decimals")
	@JsonProperty("quantity")
	private Double quantity;
	
	@NotNull(message = "ValidityYears is required ,  please enter Valid number between 1-10")
	@Min(1)
	@Max(10)
	@JsonProperty("validityYears")
	private Long validityYears;

	@JsonProperty("remarks")
	@Length( min = 1, max = 1000)
	private String remarks;

	@NotNull(message="tradeCommencementDate is required, Please enter valid date epoc")
	@JsonProperty("tradeCommencementDate")
	private Long tradeCommencementDate;

	@NotNull(message="licenseValidFromDate is required, Please enter valid date in  epoc")
	@JsonProperty("licenseValidFromDate")
	private String licenseValidFromDate;

	@JsonProperty("agreementDate")
	private Long agreementDate;

	@JsonProperty("agreementNo")
	@Length( min = 4, max = 128, message="Please enter valid agreementNo between 4-128 ")
	private String agreementNo;

	@JsonProperty("isLegacy")
	@NotNull(message="isLegacy is required, Please enter valid value true/false")
	private Boolean isLegacy = false;

	@JsonProperty("isPropertyOwner")
	@NotNull(message="isPropertyOwner is required, Please enter valid value true/false")
	private Boolean isPropertyOwner = false;
	
	@JsonProperty("active")
	private Boolean active = true;

	@JsonProperty("expiryDate")
	private Long expiryDate;

	@JsonProperty("feeDetails")
	@Valid
	private List<LicenseFeeDetailContract> feeDetails;

	@JsonProperty("supportDocuments")
	@Valid
	private List<SupportDocumentContract> supportDocuments;

	@JsonProperty("status")
	private Long status;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;

}