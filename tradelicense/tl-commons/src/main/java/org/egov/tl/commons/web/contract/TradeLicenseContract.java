package org.egov.tl.commons.web.contract;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.egov.tl.commons.web.contract.enums.ApplicationTypeEnum;
import org.egov.tl.commons.web.contract.enums.BusinessNatureEnum;
import org.egov.tl.commons.web.contract.enums.EstablishmentTypeEnum;
import org.egov.tl.commons.web.contract.enums.Gender;
import org.egov.tl.commons.web.contract.enums.OwnerShipTypeEnum;
import org.egov.tl.commons.web.contract.enums.OwnerTypeEnum;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
	@Pattern(regexp = ".*[^ ].*", message = "{error.license.tenantId.emptyspaces}")
	@NotEmpty(message = "{error.license.tenantId.empty}")
	@Length(min = 4, max = 128, message = "{error.license.tenantId.empty}")
	private String tenantId;

	private ApplicationTypeEnum applicationType;

	@Length(min = 4, max = 20, message = "{error.license.applicationnumber}")
	@JsonProperty("applicationNumber")
	private String applicationNumber;

	@JsonProperty("oldLicenseNumber")
	@Pattern(regexp = ".*[^ ].*", message = "{error.oldLicenseNumber.emptyspaces}")
	@Length(min = 1, max = 20, message = "{error.oldLicenseNumber.empty}")
	private String oldLicenseNumber;

	@Length(min = 4, max = 20, message = "{error.license.licensenumber}")
	@JsonProperty("licenseNumber")
	private String licenseNumber;

	@JsonProperty("applicationDate")
	private Long applicationDate;

	@JsonProperty("ownerAadhaarNumber")
	@Pattern(regexp = "[0-9]{12}", message = "{error.license.owneraadhaarnumber}")
	@Length(min = 12, max = 12, message = "{error.license.owneraadhaarnumber}")
	private String ownerAadhaarNumber;

	@NotEmpty(message = "{error.license.mobilenumber}")
	@JsonProperty("ownerMobileNumber")
	@Length(min = 10, max = 10, message = "{error.license.ownermobilenumber}")
	@Pattern(regexp = "[0-9]{10}", message = "{error.license.ownermobilenumber}")
	private String ownerMobileNumber;

	@NotEmpty(message = "{error.license.ownername}")
	@Pattern(regexp = ".*[^ ].*", message = "{error.license.ownername}")
	@Length(min = 3, max = 100, message = "{error.license.ownername}")
	@JsonProperty("ownerName")
	private String ownerName;

	@NotNull(message = "{error.license.ownertype}")
	@JsonProperty("ownerType")
	private OwnerTypeEnum ownerType;

	@NotNull(message = "{error.license.ownerGenderempty}")
	@JsonProperty("ownerGender")
	private Gender ownerGender;

	@Length(min = 4, max = 4, message = "{error.license.ownerbirthyear}")
	@JsonProperty("ownerBirthYear")
	private String ownerBirthYear;

	@Length(min = 3, max = 250, message = "{error.license.ownercorrespondenceaddress}")
	@JsonProperty("ownerCorrAddress")
	private String ownerCorrAddress;

	@Length(min = 3, max = 50, message = "{error.license.ownercity}")
	@JsonProperty("ownerCity")
	private String ownerCity;

	@Length(min = 6, max = 6, message = "{error.license.ownerpincode}")
	@JsonProperty("ownerPinCode")
	private String ownerPinCode;

	@NotEmpty(message = "{error.license.owneremailid}")
	@Length(min = 6, max = 50, message = "{error.license.owneremailid}")
	@Email(message = "{error.license.owneremailid}")
	@JsonProperty("ownerEmailId")
	private String ownerEmailId;

	@Length(min = 10, max = 10, message = "{error.license.ownerphonenumber}")
	@JsonProperty("ownerPhoneNumber")
	private String ownerPhoneNumber;

	@JsonProperty("ownerPhoto")
	private String ownerPhoto;

	@NotEmpty(message = "{error.license.fatherspousename}")
	@Pattern(regexp = ".*[^ ].*", message = "{error.license.fatherspousename.empty}")
	@Length(min = 3, max = 100, message = "{error.license.fatherspousename}")
	@JsonProperty("fatherSpouseName")
	private String fatherSpouseName;

	@NotEmpty(message = "{error.license.owneraddress.empty}")
	@Pattern(regexp = ".*[^ ].*", message = "{error.license.owneraddress.emptyspaces}")
	@Length(min = 3, max = 250, message = "{error.license.owneraddress}")
	@JsonProperty("ownerAddress")
	private String ownerAddress;

	@JsonProperty("establishmentType")
	private EstablishmentTypeEnum establishmentType;

	@NotEmpty(message = "{error.license.establishmentname.empty}")
	@Pattern(regexp = ".*[^ ].*", message = "{error.license.establishmentname.emptyspaces}")
	@Length(min = 3, max = 100, message = "{error.license.establishmentname}")
	@JsonProperty("establishmentName")
	private String establishmentName;

	@NotEmpty(message = "{error.license.establishmentregno.empty}")
	@Pattern(regexp = ".*[^ ].*", message = "{error.license.establishmentregno.emptyspace}")
	@Length(min = 3, max = 100, message = "{error.license.establishmentregno}")
	@JsonProperty("establishmentRegNo")
	private String establishmentRegNo;

	@NotEmpty(message = "{error.license.establishmentscorrespondenceaddress.empty}")
	@Length(min = 3, max = 250, message = "{error.license.establishmentscorrespondenceaddress}")
	@Pattern(regexp = ".*[^ ].*", message = "{error.license.establishmentscorrespondenceaddress.emptyspace}")
	@JsonProperty("establishmentCorrAddress")
	private String establishmentCorrAddress;

	@Length(min = 3, max = 50, message = "{error.license.establishmentcity}")
	@JsonProperty("establishmentCity")
	private String establishmentCity;

	@NotEmpty(message = "{error.license.establishmentpincode.empty}")
	@Length(min = 6, max = 6, message = "{error.license.establishmentpincode}")
	@Pattern(regexp = ".*[^ ].*", message = "{error.license.establishmentpincode.emptyspace}")
	@JsonProperty("establishmentPinCode")
	private String establishmentPinCode;

	@NotEmpty(message = "{error.license.establishmentphonenumber.empty}")
	@Length(min = 10, max = 10, message = "{error.license.establishmentphonenumber}")
	@Pattern(regexp = ".*[^ ].*", message = "{error.license.establishmentphonenumber.emptyspace}")
	@JsonProperty("establishmentPhoneNo")
	private String establishmentPhoneNo;

	@NotEmpty(message = "{error.license.establishmentmobilenumber.empty}")
	@Length(min = 10, max = 10, message = "{error.license.establishmentmobilenumber}")
	@Pattern(regexp = ".*[^ ].*", message = "{error.license.establishmentmobilenumber.emptyspace}")
	@JsonProperty("establishmentMobNo")
	private String establishmentMobNo;

	@NotEmpty(message = "{error.license.establishmentemailid.empty}")
	@Length(min = 6, max = 50, message = "{error.license.establishmentemailid}")
	@Pattern(regexp = ".*[^ ].*", message = "{error.license.establishmentemailid.emptyspace}")
	@JsonProperty("establishmentEmailId")
	private String establishmentEmailId;

	@JsonProperty("surveyOrGatNo")
	private String surveyOrGatNo;

	@JsonProperty("ctsOrFinalPlotNo")
	private String ctsOrFinalPlotNo;

	@JsonProperty("plotNo")
	private String plotNo;

	@JsonProperty("waterConnectionNo")
	private String waterConnectionNo;

	@Length(min = 3, max = 100, message = "{error.license.landownername}")
	@JsonProperty("landOwnerName")
	private String landOwnerName;

	@JsonProperty("isConsentLetterTaken")
	private Boolean isConsentLetterTaken;

	@NotEmpty(message = "{error.license.businessdescription.empty}")
	@JsonProperty("businessDescription")
	private String businessDescription;

	@JsonProperty("prevLicenseNo")
	private String prevLicenseNo;

	@JsonProperty("prevLicenseDate")
	private Long prevLicenseDate;

	@NotNull(message = "{error.license.totalEmployees.empty}")
	@JsonProperty("totalEmployees")
	private Integer totalEmployees;

	@JsonProperty("totalMachines")
	private Integer totalMachines;

	@NotNull(message = "{error.license.LRBFSP.empty}")
	@JsonProperty("licenseRejBefrForSamePremise")
	private Boolean licenseRejBefrForSamePremise;

	@JsonProperty("explLicenseNo")
	private String explLicenseNo;

	@JsonProperty("totalShifts")
	private Integer totalShifts;

	@Length(min = 4, max = 20, message = "{error.license.propertyassesmentno}")
	@JsonProperty("propertyAssesmentNo")
	private String propertyAssesmentNo;

	@JsonProperty("locality")
	private String locality;

	@NotEmpty(message = "{error.license.adminward}")
	@JsonProperty("adminWard")
	private String adminWard;

	@NotEmpty(message = "{error.license.revenueward}")
	@JsonProperty("revenueWard")
	private String revenueWard;

	@NotEmpty(message = "{error.license.tradeaddress}")
	@Pattern(regexp = ".*[^ ].*", message = "{error.license.tradeaddress}")
	@JsonProperty("tradeAddress")
	@Length(min = 3, max = 250, message = "{error.license.tradeaddress}")
	private String tradeAddress;

	@NotNull(message = "{error.license.ownershiptype}")
	@JsonProperty("ownerShipType")
	private OwnerShipTypeEnum ownerShipType;

	@NotNull(message = "{error.license.tradetype}")
	@JsonProperty("tradeType")
	private BusinessNatureEnum tradeType;

	@NotEmpty(message = "{error.license.tradetitle}")
	@Pattern(regexp = ".*[^ ].*", message = "{error.license.tradetitle.emptyspaces}")
	@JsonProperty("tradeTitle")
	@Length(min = 3, max = 250, message = "{error.license.tradetitle}")
	private String tradeTitle;

	@NotNull(message = "{error.license.category}")
	@JsonProperty("category")
	private String category;

	@NotNull(message = "{error.license.subcategory}")
	@JsonProperty("subCategory")
	private String subCategory;

	@NotNull(message = "{error.license.uom}")
	@JsonProperty("uom")
	private String uom;

	@NotNull(message = "{error.license.quantity}")
	@Digits(integer = 10, fraction = 2, message = "{error.license.quantity}")
	@JsonProperty("quantity")
	private Double quantity;

	@NotNull(message = "{error.license.validityyears}")
	@Min(1)
	@Max(10)
	@JsonProperty("validityYears")
	private Long validityYears;

	@JsonProperty("remarks")
	@Length(max = 250, message = "{error.license.remarks}")
	private String remarks;

	@NotNull(message = "{error.license.tradecommencementdate}")
	@JsonProperty("tradeCommencementDate")
	private Long tradeCommencementDate;

	@JsonProperty("licenseValidFromDate")
	private Long licenseValidFromDate;

	@JsonProperty("issuedDate")
	private Long issuedDate;

	@JsonProperty("agreementDate")
	private Long agreementDate;

	@JsonProperty("agreementNo")
	private String agreementNo;

	@JsonProperty("isLegacy")
	@NotNull(message = "{error.license.islegacy}")
	private Boolean isLegacy = false;

	@JsonProperty("isPropertyOwner")
	@NotNull(message = "{error.license.ispropertyowner}")
	private Boolean isPropertyOwner = false;

	@JsonProperty("active")
	@NotNull(message = "{error.license.active}")
	private Boolean active = true;

	@JsonProperty("expiryDate")
	private Long expiryDate;

	@JsonProperty("userId")
	private Long userId;

	@JsonProperty("isDataPorting")
	private Boolean isDataPorting = false;

	@JsonProperty("status")
	private String status;

	private String billId;

	@JsonProperty("application")
	private LicenseApplicationContract application;

	@JsonProperty("feeDetails")
	@Valid
	private List<LicenseFeeDetailContract> feeDetails;

	@JsonProperty("supportDocuments")
	@Valid
	private List<SupportDocumentContract> supportDocuments;

	@JsonProperty("partners")
	@Valid
	private List<TradePartnerContract> partners;

	@JsonProperty("shifts")
	@Valid
	private List<TradeShiftContract> shifts;

	@JsonIgnore
	@JsonProperty("licenseData")
	private Map<String, Object> licenseData;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;
}