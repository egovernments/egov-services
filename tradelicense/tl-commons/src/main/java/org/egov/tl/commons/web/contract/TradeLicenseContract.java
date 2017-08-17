package org.egov.tl.commons.web.contract;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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
	@Size( min =20, max =20, message="oldLicenseNumber is required, Please enter valid Old License Number with 20 characters")
	private String oldLicenseNumber;

	@NotEmpty(message="applicationDate is required, Please enter valid date in dd/mm/yyyy")
	@Pattern(regexp = "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)", message="applicationDate is required, Please enter valid date in dd/mm/yyyy")
	@Length( min =10, max =10, message="applicationDate is required, Please enter valid date in dd/mm/yyyy")
	@JsonProperty("applicationDate")
	private String applicationDate;

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
	@Length( min = 1, max = 50, message="Emailid is required. Please enter the valid emailid")
	@Email(message="Emailid is required. Please enter the valid emailid")
	@JsonProperty("emailId")
	private String emailId;

	@NotEmpty(message = "Owner Address  is required ,  please enter Valid Address length between 4-250 characters")
	@Length( min = 4, max = 250, message = "Owner Address  is required ,  please enter Valid Address length between 4-250 characters")
	@JsonProperty("ownerAddress")
	private String ownerAddress;

	@JsonProperty("propertyAssesmentNo")
	@Length( min = 15, max = 20, message = "Property Assesment No  is required ,  please enter Valid Property Assesment No  between 15-20 characters")
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

	@NotEmpty(message="tradeCommencementDate is required, Please enter valid date in dd/mm/yyyy")
	@Pattern(regexp = "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)" , message="tradeCommencementDate is required, Please enter valid date in dd/mm/yyyy")
	@Length( min =10, max =10)
	@JsonProperty("tradeCommencementDate")
	private String tradeCommencementDate;

	@NotEmpty(message="licenseValidFromDate is required, Please enter valid date in  dd/mm/yyyy")
	@Pattern(regexp = "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)", message="licenseValidFromDate is required, Please enter valid date in  dd/mm/yyyy")
	@Length( min =10, max =10)
	@JsonProperty("licenseValidFromDate")
	private String licenseValidFromDate;

	@JsonProperty("agreementDate")
	@Pattern(regexp = "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)",message="agreementDate is required, Please enter valid date in dd/mm/yyyy")
	private String agreementDate;

	@JsonProperty("agreementNo")
	@Length( min = 4, max = 128, message="Please enter valid agreementNo between 4-128 ")
	private String agreementNo;

	@JsonProperty("isLegacy")
	private Boolean isLegacy = false;

	@JsonProperty("isTradeOwner")
	private Boolean isTradeOwner = false;
	
	@JsonProperty("active")
	private Boolean active = true;

	@JsonProperty("expiryDate")
	private String expiryDate;

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