package org.egov.tradelicense.persistence.entity;

import java.util.List;

import org.egov.tradelicense.domain.enums.ApplicationType;
import org.egov.tradelicense.domain.enums.BusinessNature;
import org.egov.tradelicense.domain.enums.OwnerShipType;
import org.egov.tradelicense.domain.model.AuditDetails;
import org.egov.tradelicense.domain.model.LicenseFeeDetail;
import org.egov.tradelicense.domain.model.SupportDocument;
import org.egov.tradelicense.domain.model.TradeLicense;

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

	private String applicationType;

	private String applicationNumber;

	private String licenseNumber;

	private String oldLicenseNumber;

	private String applicationDate;

	private String adhaarNumber;

	private String mobileNumber;

	private String ownerName;

	private String fatherSpouseName;

	private String emailId;

	private String ownerAddress;

	private String propertyAssesmentNo;

	private Integer localityId;

	private Integer wardId;

	private String tradeAddress;

	private String ownerShipType;

	private String tradeTitle;

	private String tradeType;

	private Long categoryId;

	private Long subCategoryId;

	private Long uomId;

	private Double quantity;

	private String remarks;

	private String tradeCommencementDate;

	private String agrementDate;

	private String agrementNo;

	private Boolean isLegacy = false;

	private Boolean active = true;

	private String expiryDate;

	private List<LicenseFeeDetail> feeDetails;

	private List<SupportDocument> supportDocuments;

	private String createdBy;

	private String lastModifiedBy;

	private Long createdTime;

	private Long lastModifiedTime;

	public TradeLicense toDomain() {

		TradeLicense tradeLicense = new TradeLicense();

		AuditDetails auditDetails = new AuditDetails();

		tradeLicense.setId(this.id);

		tradeLicense.setTenantId(this.tenantId);

		tradeLicense.setApplicationType(ApplicationType.valueOf(this.applicationType));

		tradeLicense.setApplicationNumber(this.applicationNumber);

		tradeLicense.setLicenseNumber(this.licenseNumber);

		tradeLicense.setOldLicenseNumber(this.oldLicenseNumber);

		tradeLicense.setApplicationDate(this.applicationDate);

		tradeLicense.setAdhaarNumber(this.adhaarNumber);

		tradeLicense.setMobileNumber(this.mobileNumber);

		tradeLicense.setOwnerName(this.ownerName);

		tradeLicense.setFatherSpouseName(this.fatherSpouseName);

		tradeLicense.setEmailId(this.emailId);

		tradeLicense.setOwnerAddress(this.ownerAddress);

		tradeLicense.setPropertyAssesmentNo(this.propertyAssesmentNo);

		tradeLicense.setLocalityId(this.localityId);

		tradeLicense.setWardId(this.wardId);

		tradeLicense.setTradeAddress(this.tradeAddress);

		tradeLicense.setOwnerShipType(OwnerShipType.valueOf(this.ownerShipType));

		tradeLicense.setTradeTitle(this.tradeTitle);

		tradeLicense.setTradeType(BusinessNature.valueOf(this.tradeType));

		tradeLicense.setCategoryId(this.categoryId);

		tradeLicense.setSubCategoryId(this.subCategoryId);

		tradeLicense.setUomId(this.uomId);

		tradeLicense.setQuantity(this.quantity);

		tradeLicense.setRemarks(this.remarks);

		tradeLicense.setTradeCommencementDate(this.tradeCommencementDate);

		tradeLicense.setAgrementDate(this.agrementDate);

		tradeLicense.setAgrementNo(this.agrementNo);

		tradeLicense.setIsLegacy(this.isLegacy);

		tradeLicense.setActive(this.active);

		tradeLicense.setExpiryDate(this.expiryDate);

		tradeLicense.setFeeDetails(this.feeDetails);

		tradeLicense.setSupportDocuments(this.supportDocuments);

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

		this.applicationType = tradeLicense.getApplicationType().toString();

		this.applicationNumber = tradeLicense.getApplicationNumber();

		this.licenseNumber = tradeLicense.getLicenseNumber();

		this.oldLicenseNumber = tradeLicense.getOldLicenseNumber();

		this.applicationDate = tradeLicense.getApplicationDate();

		this.adhaarNumber = tradeLicense.getAdhaarNumber();

		this.mobileNumber = tradeLicense.getMobileNumber();

		this.ownerName = tradeLicense.getOwnerName();

		this.fatherSpouseName = tradeLicense.getFatherSpouseName();

		this.emailId = tradeLicense.getEmailId();

		this.ownerAddress = tradeLicense.getOwnerAddress();

		this.propertyAssesmentNo = tradeLicense.getPropertyAssesmentNo();

		this.localityId = tradeLicense.getLocalityId();

		this.wardId = tradeLicense.getWardId();

		this.tradeAddress = tradeLicense.getTradeAddress();

		this.ownerShipType = tradeLicense.getOwnerShipType().toString();

		this.tradeTitle = tradeLicense.getTradeTitle();

		this.tradeType = tradeLicense.getTradeType().toString();

		this.categoryId = tradeLicense.getCategoryId();

		this.subCategoryId = tradeLicense.getSubCategoryId();

		this.uomId = tradeLicense.getUomId();

		this.quantity = tradeLicense.getQuantity();

		this.remarks = tradeLicense.getRemarks();

		this.tradeCommencementDate = tradeLicense.getTradeCommencementDate();

		this.agrementDate = tradeLicense.getAgrementDate();

		this.agrementNo = tradeLicense.getAgrementNo();

		this.isLegacy = tradeLicense.getIsLegacy();

		this.active = tradeLicense.getActive();

		this.expiryDate = tradeLicense.getExpiryDate();

		this.feeDetails = tradeLicense.getFeeDetails();

		this.supportDocuments = tradeLicense.getSupportDocuments();

		this.createdBy = (auditDetails == null) ? null : auditDetails.getCreatedBy();

		this.lastModifiedBy = (auditDetails == null) ? null : auditDetails.getLastModifiedBy();

		this.createdTime = (auditDetails == null) ? null : auditDetails.getCreatedTime();

		this.lastModifiedTime = (auditDetails == null) ? null : auditDetails.getLastModifiedTime();

		return this;
	}

}
