package org.egov.tl.commons.web.contract;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.egov.tl.commons.web.contract.enums.ApplicationTypeEnum;
import org.egov.tl.commons.web.contract.enums.BusinessNatureEnum;
import org.egov.tl.commons.web.contract.enums.OwnerShipTypeEnum;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TradeLicenseSearchContract {

	private Long id;

	@JsonProperty("tenantId")
	@NotNull
	private String tenantId;

	@JsonProperty("applicationType")
	private ApplicationTypeEnum applicationType;

	@JsonProperty("applicationNumber")
	private String applicationNumber;

	@JsonProperty("oldLicenseNumber")
	private String oldLicenseNumber;

	@JsonProperty("licenseNumber")
	private String licenseNumber;

	@JsonProperty("applicationDate")
	private String applicationDate;

	@JsonProperty("ownerAadhaarNumber")
	private String ownerAadhaarNumber;

	@JsonProperty("ownerMobileNumber")
	private String ownerMobileNumber;

	@JsonProperty("ownerName")
	private String ownerName;

	@JsonProperty("ownerType")
	private String ownerType;

	@JsonProperty("ownerGender")
	private String ownerGender;

	@JsonProperty("ownerBirthYear")
	private String ownerBirthYear;

	@JsonProperty("ownerCorrAddress")
	private String ownerCorrAddress;

	@JsonProperty("ownerCity")
	private String ownerCity;

	@JsonProperty("ownerPinCode")
	private String ownerPinCode;

	@JsonProperty("ownerEmailId")
	private String ownerEmailId;

	@JsonProperty("ownerPhoneNumber")
	private String ownerPhoneNumber;

	@JsonProperty("ownerPhoto")
	private String ownerPhoto;

	@JsonProperty("fatherSpouseName")
	private String fatherSpouseName;

	@JsonProperty("ownerAddress")
	private String ownerAddress;

	@JsonProperty("establishmentType")
	private String establishmentType;

	@JsonProperty("establishmentName")
	private String establishmentName;

	@JsonProperty("establishmentRegNo")
	private String establishmentRegNo;

	@JsonProperty("establishmentCorrAddress")
	private String establishmentCorrAddress;

	@JsonProperty("establishmentCity")
	private String establishmentCity;

	@JsonProperty("establishmentPinCode")
	private String establishmentPinCode;

	@JsonProperty("establishmentPhoneNo")
	private String establishmentPhoneNo;

	@JsonProperty("establishmentMobNo")
	private String establishmentMobNo;

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

	@JsonProperty("landOwnerName")
	private String landOwnerName;

	@JsonProperty("isConsentLetterTaken")
	private Boolean isConsentLetterTaken;

	@JsonProperty("businessDescription")
	private String businessDescription;

	@JsonProperty("prevLicenseNo")
	private String prevLicenseNo;

	@JsonProperty("prevLicenseDate")
	private Long prevLicenseDate;

	@JsonProperty("totalEmployees")
	private Integer totalEmployees;

	@JsonProperty("totalMachines")
	private Integer totalMachines;

	@JsonProperty("licenseRejBefrForSamePremise")
	private Boolean licenseRejBefrForSamePremise;

	@JsonProperty("explLicenseNo")
	private String explLicenseNo;

	@JsonProperty("totalShifts")
	private Integer totalShifts;

	@JsonProperty("propertyAssesmentNo")
	private String propertyAssesmentNo;

	@JsonProperty("locality")
	private String locality;

	@JsonProperty("localityName")
	private String localityName;

	@JsonProperty("adminWard")
	private String adminWard;

	@JsonProperty("adminWardName")
	private String adminWardName;

	@JsonProperty("revenueWard")
	private String revenueWard;

	@JsonProperty("revenueWardName")
	private String revenueWardName;

	@JsonProperty("tradeAddress")
	private String tradeAddress;

	@JsonProperty("ownerShipType")
	private OwnerShipTypeEnum ownerShipType;

	@JsonProperty("tradeTitle")
	private String tradeTitle;

	@JsonProperty("tradeType")
	private BusinessNatureEnum tradeType;

	@JsonProperty("category")
	private String category;

	@JsonProperty("categoryName")
	private String categoryName;

	@JsonProperty("subCategory")
	private String subCategory;

	@JsonProperty("subCategoryName")
	private String subCategoryName;

	@JsonProperty("uom")
	private String uom;

	@JsonProperty("uomName")
	private String uomName;
	
	@JsonProperty("status")
	private String status;
	
	@JsonProperty("statusName")
	private String statusName;

	@JsonProperty("quantity")
	private Double quantity;

	@JsonProperty("remarks")
	private String remarks;

	@JsonProperty("tradeCommencementDate")
	private String tradeCommencementDate;

	@JsonProperty("issuedDate")
	private String issuedDate;

	@JsonProperty("licenseValidFromDate")
	private String licenseValidFromDate;

	@JsonProperty("isPropertyOwner")
	private Boolean isPropertyOwner = false;

	@JsonProperty("agreementDate")
	private String agreementDate;
	
	@JsonProperty("agreementNo")
	private String agreementNo;

	@JsonProperty("isLegacy")
	private Boolean isLegacy = false;

	@JsonProperty("active")
	private Boolean active = true;

	@JsonProperty("expiryDate")
	private String expiryDate;

	@JsonProperty("feeDetails")
	private List<LicenseFeeDetailContract> feeDetails;

	@JsonProperty("supportDocuments")
	private List<SupportDocumentSearchContract> supportDocuments;

	@JsonProperty("applications")
	private List<LicenseApplicationSearchContract> applications;
	
	@JsonProperty("partners")
	private List<TradePartnerContract> partners;

	@JsonProperty("shifts")
	private List<TradeShiftContract> shifts;

	@JsonProperty("cityCode")
	private String cityCode;

	@JsonProperty("cityDistrictCode")
	private String cityDistrictCode;

	@JsonProperty("cityDistrictName")
	private String cityDistrictName;

	@JsonProperty("cityGrade")
	private String cityGrade;

	@JsonProperty("cityRegionName")
	private String cityRegionName;

	@JsonProperty("cityName")
	private String cityName;

	@JsonProperty("validityYears")
	private Long validityYears;

	@JsonProperty("userid")
	private Long userId;

	@JsonIgnore
	@JsonProperty("licenseData")
	private Map<String, Object> licenseData;
	
	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;

	
}