package org.egov.tradelicense.persistence.entity;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;

import org.egov.tradelicense.domain.enums.ApplicationType;
import org.egov.tradelicense.domain.enums.BusinessNature;
import org.egov.tradelicense.domain.enums.EstablishmentType;
import org.egov.tradelicense.domain.enums.Gender;
import org.egov.tradelicense.domain.enums.OwnerShipType;
import org.egov.tradelicense.domain.enums.OwnerType;
import org.egov.tradelicense.domain.model.AuditDetails;
import org.egov.tradelicense.domain.model.TradeLicense;
import org.postgresql.util.PGobject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeLicenseEntity {

	public static final String TABLE_NAME = "egtl_license";
	public static final String SEQUENCE_NAME = "seq_egtl_license";

	private Long id;

	private String tenantId;

	protected String applicationType;

	protected String applicationNumber;

	private String oldLicenseNumber;

	private String licenseNumber;

	protected Timestamp applicationDate;

	private String ownerAadhaarNumber;

	private String ownerMobileNumber;

	private String ownerName;

	private String ownerType;

	private String ownerGender;

	private String ownerBirthYear;

	private String ownerCorrAddress;

	private String ownerCity;

	private String ownerPinCode;

	private String ownerEmailId;

	private String ownerPhoneNumber;

	private String ownerPhoto;

	private String fatherSpouseName;

	private String ownerAddress;

	private String establishmentType;

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

	private String ownerShipType;

	private String tradeType;

	private String tradeTitle;

	private String category;

	private String subCategory;

	private String uom;

	private Double quantity;

	private Long validityYears;

	private String remarks;

	private Timestamp tradeCommencementDate;

	private Timestamp licenseValidFromDate;

	private Timestamp issuedDate;

	private Timestamp agreementDate;

	private String agreementNo;

	private Boolean isLegacy = false;

	private Boolean isPropertyOwner = false;

	private Boolean active = true;

	private Timestamp expiryDate;

	private String status;

	private PGobject licenseData;

	private Long userId;

	private String createdBy;

	private String lastModifiedBy;

	private Long createdTime;

	private Long lastModifiedTime;

	public TradeLicense toDomain() {

		TradeLicense tradeLicense = new TradeLicense();

		AuditDetails auditDetails = new AuditDetails();

		tradeLicense.setId(this.id);

		tradeLicense.setTenantId(this.tenantId);

		if (this.applicationType != null && !this.applicationType.isEmpty()) {

			tradeLicense.setApplicationType(ApplicationType.valueOf(this.applicationType));
		}

		tradeLicense.setApplicationNumber(this.applicationNumber);

		tradeLicense.setOldLicenseNumber(this.oldLicenseNumber);

		tradeLicense.setLicenseNumber(this.licenseNumber);

		if (this.applicationDate != null) {

			tradeLicense.setApplicationDate((this.applicationDate.getTime()));
		}

		tradeLicense.setOwnerAadhaarNumber(this.ownerAadhaarNumber);

		tradeLicense.setOwnerMobileNumber(this.ownerMobileNumber);

		tradeLicense.setOwnerName(this.ownerName);

		if (this.ownerType != null && !this.ownerType.isEmpty()) {

			tradeLicense.setOwnerType(OwnerType.valueOf(this.ownerType));
		}

		if (this.ownerGender != null && !this.ownerGender.isEmpty()) {

			tradeLicense.setOwnerGender(Gender.valueOf(this.ownerGender));
		}

		tradeLicense.setOwnerBirthYear(this.ownerBirthYear);

		tradeLicense.setOwnerCorrAddress(this.ownerCorrAddress);

		tradeLicense.setOwnerCity(this.ownerCity);

		tradeLicense.setOwnerPinCode(this.ownerPinCode);

		tradeLicense.setOwnerEmailId(this.ownerEmailId);

		tradeLicense.setOwnerPhoneNumber(this.ownerPhoneNumber);

		tradeLicense.setOwnerPhoto(this.ownerPhoto);

		tradeLicense.setFatherSpouseName(this.fatherSpouseName);

		tradeLicense.setOwnerAddress(this.ownerAddress);

		if (this.establishmentType != null && !this.establishmentType.isEmpty()) {

			tradeLicense.setEstablishmentType(EstablishmentType.valueOf(this.establishmentType));
		}

		tradeLicense.setEstablishmentName(this.establishmentName);

		tradeLicense.setEstablishmentRegNo(this.establishmentRegNo);

		tradeLicense.setEstablishmentCorrAddress(this.establishmentCorrAddress);

		tradeLicense.setEstablishmentCity(this.establishmentCity);

		tradeLicense.setEstablishmentPinCode(this.establishmentPinCode);

		tradeLicense.setEstablishmentPhoneNo(this.establishmentPhoneNo);

		tradeLicense.setEstablishmentMobNo(this.establishmentMobNo);

		tradeLicense.setEstablishmentEmailId(this.establishmentEmailId);

		tradeLicense.setSurveyOrGatNo(this.surveyOrGatNo);

		tradeLicense.setCtsOrFinalPlotNo(this.ctsOrFinalPlotNo);

		tradeLicense.setPlotNo(this.plotNo);

		tradeLicense.setWaterConnectionNo(this.waterConnectionNo);

		tradeLicense.setLandOwnerName(this.landOwnerName);

		tradeLicense.setIsConsentLetterTaken(this.isConsentLetterTaken);

		tradeLicense.setBusinessDescription(this.businessDescription);

		tradeLicense.setPrevLicenseNo(this.prevLicenseNo);

		tradeLicense.setPrevLicenseDate(this.prevLicenseDate);

		tradeLicense.setTotalEmployees(this.totalEmployees);

		tradeLicense.setTotalMachines(this.totalMachines);

		tradeLicense.setLicenseRejBefrForSamePremise(this.licenseRejBefrForSamePremise);

		tradeLicense.setExplLicenseNo(this.explLicenseNo);

		tradeLicense.setTotalShifts(this.totalShifts);

		tradeLicense.setPropertyAssesmentNo(this.propertyAssesmentNo);

		tradeLicense.setLocality(this.locality);

		tradeLicense.setAdminWard(this.adminWard);

		tradeLicense.setRevenueWard(this.revenueWard);

		tradeLicense.setTradeAddress(this.tradeAddress);

		if (this.ownerShipType != null && !this.ownerShipType.isEmpty()) {

			tradeLicense.setOwnerShipType(OwnerShipType.valueOf(this.ownerShipType));
		}

		if (this.tradeType != null && !this.tradeType.isEmpty()) {

			tradeLicense.setTradeType(BusinessNature.valueOf(this.tradeType));
		}

		tradeLicense.setTradeTitle(this.tradeTitle);

		tradeLicense.setCategory(this.category);

		tradeLicense.setSubCategory(this.subCategory);

		tradeLicense.setUom(this.uom);

		tradeLicense.setQuantity(this.quantity);

		tradeLicense.setValidityYears(this.validityYears);

		tradeLicense.setRemarks(this.remarks);

		if (this.tradeCommencementDate != null) {

			tradeLicense.setTradeCommencementDate((this.tradeCommencementDate.getTime()));
		}

		if (this.licenseValidFromDate != null) {

			tradeLicense.setLicenseValidFromDate((this.licenseValidFromDate.getTime()));
		}

		if (this.issuedDate != null) {

			tradeLicense.setIssuedDate((this.issuedDate.getTime()));
		}

		if (this.agreementDate != null) {

			tradeLicense.setAgreementDate((this.agreementDate.getTime()));
		}

		tradeLicense.setAgreementNo(this.agreementNo);

		tradeLicense.setIsLegacy(this.isLegacy);

		tradeLicense.setIsPropertyOwner(this.isPropertyOwner);

		tradeLicense.setActive(this.active);

		tradeLicense.setStatus(this.status);

		if (this.expiryDate != null) {

			tradeLicense.setExpiryDate((this.expiryDate.getTime()));
		}

		if (this.licenseData != null && this.isLegacy != null && (this.isLegacy == Boolean.TRUE)) {

			Gson gson = new GsonBuilder().serializeNulls().create();
			tradeLicense.setLicenseData(gson.fromJson(this.licenseData.toString(), Map.class));
		}

		tradeLicense.setUserId(this.userId);

		auditDetails.setCreatedBy(this.createdBy);

		auditDetails.setCreatedTime(this.createdTime);

		auditDetails.setLastModifiedBy(this.lastModifiedBy);

		auditDetails.setLastModifiedTime(this.lastModifiedTime);

		tradeLicense.setAuditDetails(auditDetails);

		return tradeLicense;
	}

	public TradeLicenseEntity toEntity(TradeLicense tradeLicense) {

		AuditDetails auditDetails = tradeLicense.getAuditDetails();

		this.id = tradeLicense.getId();

		this.tenantId = tradeLicense.getTenantId();

		if (tradeLicense.getApplicationType() != null) {

			this.applicationType = tradeLicense.getApplicationType().toString();
		}

		this.applicationNumber = tradeLicense.getApplicationNumber();

		this.oldLicenseNumber = tradeLicense.getOldLicenseNumber();

		this.licenseNumber = tradeLicense.getLicenseNumber();

		if (tradeLicense.getApplicationDate() != null) {

			this.applicationDate = new Timestamp(tradeLicense.getApplicationDate());
		}

		this.ownerAadhaarNumber = tradeLicense.getOwnerAadhaarNumber();

		this.ownerMobileNumber = tradeLicense.getOwnerMobileNumber();

		this.ownerName = tradeLicense.getOwnerName();

		if (tradeLicense.getOwnerType() != null) {

			this.ownerType = tradeLicense.getOwnerType().toString();
		}

		if (tradeLicense.getOwnerGender() != null) {

			this.ownerGender = tradeLicense.getOwnerGender().toString();
		}

		this.ownerBirthYear = tradeLicense.getOwnerBirthYear();

		this.ownerCorrAddress = tradeLicense.getOwnerCorrAddress();

		this.ownerCity = tradeLicense.getOwnerCity();

		this.ownerPinCode = tradeLicense.getOwnerPinCode();

		this.ownerEmailId = tradeLicense.getOwnerEmailId();

		this.ownerPhoneNumber = tradeLicense.getOwnerPhoneNumber();

		this.ownerPhoto = tradeLicense.getOwnerPhoto();

		this.fatherSpouseName = tradeLicense.getFatherSpouseName();

		this.ownerAddress = tradeLicense.getOwnerAddress();

		if (tradeLicense.getEstablishmentType() != null) {

			this.establishmentType = tradeLicense.getEstablishmentType().toString();
		}

		this.establishmentName = tradeLicense.getEstablishmentName();

		this.establishmentRegNo = tradeLicense.getEstablishmentRegNo();

		this.establishmentCorrAddress = tradeLicense.getEstablishmentCorrAddress();

		this.establishmentCity = tradeLicense.getEstablishmentCity();

		this.establishmentPinCode = tradeLicense.getEstablishmentPinCode();

		this.establishmentPhoneNo = tradeLicense.getEstablishmentPhoneNo();

		this.establishmentMobNo = tradeLicense.getEstablishmentMobNo();

		this.establishmentEmailId = tradeLicense.getEstablishmentEmailId();

		this.surveyOrGatNo = tradeLicense.getSurveyOrGatNo();

		this.ctsOrFinalPlotNo = tradeLicense.getCtsOrFinalPlotNo();

		this.plotNo = tradeLicense.getPlotNo();

		this.waterConnectionNo = tradeLicense.getWaterConnectionNo();

		this.landOwnerName = tradeLicense.getLandOwnerName();

		this.isConsentLetterTaken = tradeLicense.getIsConsentLetterTaken();

		this.businessDescription = tradeLicense.getBusinessDescription();

		this.prevLicenseNo = tradeLicense.getPrevLicenseNo();

		this.prevLicenseDate = tradeLicense.getPrevLicenseDate();

		this.totalEmployees = tradeLicense.getTotalEmployees();

		this.totalMachines = tradeLicense.getTotalMachines();

		this.licenseRejBefrForSamePremise = tradeLicense.getLicenseRejBefrForSamePremise();

		this.explLicenseNo = tradeLicense.getExplLicenseNo();

		this.totalShifts = tradeLicense.getTotalShifts();

		this.propertyAssesmentNo = tradeLicense.getPropertyAssesmentNo();

		this.locality = tradeLicense.getLocality();

		this.adminWard = tradeLicense.getAdminWard();

		this.revenueWard = tradeLicense.getRevenueWard();

		this.tradeAddress = tradeLicense.getTradeAddress();

		if (tradeLicense.getOwnerShipType() != null) {

			this.ownerShipType = tradeLicense.getOwnerShipType().toString();
		}

		if (tradeLicense.getTradeType() != null) {

			this.tradeType = tradeLicense.getTradeType().toString();
		}

		this.tradeTitle = tradeLicense.getTradeTitle();

		this.category = tradeLicense.getCategory();

		this.subCategory = tradeLicense.getSubCategory();

		this.uom = tradeLicense.getUom();

		this.quantity = tradeLicense.getQuantity();

		this.validityYears = tradeLicense.getValidityYears();

		this.remarks = tradeLicense.getRemarks();

		if (tradeLicense.getTradeCommencementDate() != null) {

			this.tradeCommencementDate = new Timestamp(tradeLicense.getTradeCommencementDate());
		}

		if (tradeLicense.getLicenseValidFromDate() != null) {

			this.licenseValidFromDate = new Timestamp(tradeLicense.getLicenseValidFromDate());
		}

		if (tradeLicense.getIssuedDate() != null) {

			this.issuedDate = new Timestamp(tradeLicense.getIssuedDate());
		}

		if (tradeLicense.getAgreementDate() != null) {

			this.agreementDate = new Timestamp(tradeLicense.getAgreementDate());
		}

		this.agreementNo = tradeLicense.getAgreementNo();

		this.isLegacy = tradeLicense.getIsLegacy();

		this.isPropertyOwner = tradeLicense.getIsPropertyOwner();

		this.active = tradeLicense.getActive();

		if (tradeLicense.getExpiryDate() != null) {

			this.expiryDate = new Timestamp(tradeLicense.getExpiryDate());
		}

		this.status = tradeLicense.getStatus();

		if (tradeLicense.getLicenseData() != null && !tradeLicense.getLicenseData().isEmpty()
				&& tradeLicense.getIsLegacy()) {

			Gson gson = new GsonBuilder().serializeNulls().create();
			String data = gson.toJson(tradeLicense.getLicenseData());
			PGobject jsonObject = new PGobject();
			jsonObject.setType("jsonb");

			try {

				jsonObject.setValue(data);

			} catch (SQLException e) {

				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			this.licenseData = jsonObject;

		} else {

			this.licenseData = null;
		}

		this.userId = tradeLicense.getUserId();

		this.createdBy = (auditDetails == null) ? null : auditDetails.getCreatedBy();

		this.lastModifiedBy = (auditDetails == null) ? null : auditDetails.getLastModifiedBy();

		this.createdTime = (auditDetails == null) ? null : auditDetails.getCreatedTime();

		this.lastModifiedTime = (auditDetails == null) ? null : auditDetails.getLastModifiedTime();

		return this;
	}

}
