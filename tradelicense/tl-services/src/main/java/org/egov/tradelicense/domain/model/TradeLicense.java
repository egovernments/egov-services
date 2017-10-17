package org.egov.tradelicense.domain.model;

import java.util.List;
import java.util.Map;

import org.egov.tradelicense.domain.enums.ApplicationType;
import org.egov.tradelicense.domain.enums.BusinessNature;
import org.egov.tradelicense.domain.enums.EstablishmentType;
import org.egov.tradelicense.domain.enums.Gender;
import org.egov.tradelicense.domain.enums.OwnerShipType;
import org.egov.tradelicense.domain.enums.OwnerType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TradeLicense {

	private Long id;

	private String tenantId;

	private ApplicationType applicationType;

	private String applicationNumber;

	private String oldLicenseNumber;

	private String licenseNumber;

	private Long applicationDate;

	private String ownerAadhaarNumber;

	private String ownerMobileNumber;

	private String ownerName;

	private OwnerType ownerType;

	private Gender ownerGender;

	private String ownerBirthYear;

	private String ownerCorrAddress;

	private String ownerCity;

	private String ownerPinCode;

	private String ownerEmailId;

	private String ownerPhoneNumber;

	private String ownerPhoto;

	private String fatherSpouseName;

	private String ownerAddress;

	private EstablishmentType establishmentType;

	private String establishmentName;

	private String establishmentRegNo;

	private String establishmentCorrAddress;

	private String establishmentCity;

	private String establishmentPinCode;

	private String establishmentPhoneNo;

	private String establishmentMobNo;

	private String establishmentEmailId;

	private String surveyOrGatNo;

	private String ctsOrFinalPlotNo;

	private String plotNo;

	private String waterConnectionNo;

	private String landOwnerName;

	private Boolean isConsentLetterTaken;

	private String businessDescription;

	private String prevLicenseNo;

	private Long prevLicenseDate;

	private Integer totalEmployees;

	private Integer totalMachines;

	private Boolean licenseRejBefrForSamePremise;

	private String explLicenseNo;

	private Integer totalShifts;

	private String propertyAssesmentNo;

	private String locality;

	private String adminWard;

	private String revenueWard;

	private String tradeAddress;

	private OwnerShipType ownerShipType;

	private BusinessNature tradeType;

	private String tradeTitle;

	private String category;

	private String subCategory;

	private String uom;

	private Double quantity;

	private Long validityYears;

	private String remarks;

	private Long tradeCommencementDate;

	private Long licenseValidFromDate;

	private Long issuedDate;

	private Long agreementDate;

	private String agreementNo;

	private Boolean isLegacy = false;

	private Boolean isPropertyOwner = false;

	private Boolean active = true;

	private Long expiryDate;

	private Boolean isDataPorting = false;

	private String status;

	private String billId;

	private Long userId;
	
	private Map<String, Object> licenseData;

	private LicenseApplication application;

	private List<LicenseApplication> applications;

	private List<LicenseFeeDetail> feeDetails;

	private List<LicenseApplicationBill> applicationLicenseBills;

	private List<SupportDocument> supportDocuments;

	private List<TradePartner> partners;

	private List<TradeShift> shifts;

	private AuditDetails auditDetails;
}