package org.egov.tradelicense.persistence.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.egov.tradelicense.domain.enums.ApplicationType;
import org.egov.tradelicense.domain.enums.BusinessNature;
import org.egov.tradelicense.domain.enums.OwnerShipType;
import org.egov.tradelicense.domain.model.AuditDetails;
import org.egov.tradelicense.domain.model.LicenseFeeDetailSearch;
import org.egov.tradelicense.domain.model.SupportDocumentSearch;
import org.egov.tradelicense.domain.model.TradeLicenseSearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeLicenseSearchEntity {

	public static final String TABLE_NAME = "egtl_license";
	public static final String SEQUENCE_NAME = "seq_egtl_license";

	private Long id;

	private String tenantId;

	private String applicationType;

	private String applicationNumber;

	private String licenseNumber;

	private String oldLicenseNumber;

	private Timestamp applicationDate;

	private String adhaarNumber;

	private String mobileNumber;

	private String ownerName;

	private String fatherSpouseName;

	private String emailId;

	private String ownerAddress;

	private String propertyAssesmentNo;

	private Integer localityId;

	private String localityName;

	private Integer revenueWardId;

	private String revenueWardName;

	private Integer adminWardId;

	private String adminWardName;

	private String tradeAddress;

	private String ownerShipType;

	private String tradeTitle;

	private String tradeType;

	private Long categoryId;

	private String category;

	private Long subCategoryId;

	private String subCategory;

	private Long uomId;

	private String uom;

	private Double quantity;

	private Long validityYears;

	private String remarks;

	private Timestamp tradeCommencementDate;

	private Timestamp agreementDate;

	private Boolean isPropertyOwner = false;

	private Timestamp licenseValidFromDate;

	private Long status;

	private String statusName;

	private String agreementNo;

	private Boolean isLegacy = false;

	private Boolean active = true;

	private Timestamp expiryDate;

	private static List<LicenseFeeDetailSearch> feeDetails;

	private static List<SupportDocumentSearch> supportDocuments;

	private List<LicenseFeeDetailSearchEntity> feeDetailEntitys;

	private List<SupportDocumentSearchEntity> supportDocumentEntitys;

	private String createdBy;

	private String lastModifiedBy;

	private Long createdTime;

	private Long lastModifiedTime;

	public TradeLicenseSearch toDomain() {

		TradeLicenseSearch tradeLicenseSearch = new TradeLicenseSearch();

		AuditDetails auditDetails = new AuditDetails();

		tradeLicenseSearch.setId(this.id);

		tradeLicenseSearch.setTenantId(this.tenantId);

		if (this.applicationType != null) {

			tradeLicenseSearch.setApplicationType(ApplicationType.valueOf(this.applicationType));
		}

		tradeLicenseSearch.setApplicationNumber(this.applicationNumber);

		tradeLicenseSearch.setLicenseNumber(this.licenseNumber);

		tradeLicenseSearch.setOldLicenseNumber(this.oldLicenseNumber);

		if (this.applicationDate != null) {

			tradeLicenseSearch.setApplicationDate((this.applicationDate.getTime()));
		}

		tradeLicenseSearch.setAdhaarNumber(this.adhaarNumber);

		tradeLicenseSearch.setMobileNumber(this.mobileNumber);

		tradeLicenseSearch.setOwnerName(this.ownerName);

		tradeLicenseSearch.setFatherSpouseName(this.fatherSpouseName);

		tradeLicenseSearch.setEmailId(this.emailId);

		tradeLicenseSearch.setOwnerAddress(this.ownerAddress);

		tradeLicenseSearch.setPropertyAssesmentNo(this.propertyAssesmentNo);

		tradeLicenseSearch.setLocalityId(this.localityId);

		tradeLicenseSearch.setLocalityName(this.localityName);

		tradeLicenseSearch.setRevenueWardId(this.revenueWardId);

		tradeLicenseSearch.setRevenueWardName(this.revenueWardName);

		tradeLicenseSearch.setAdminWardId(this.adminWardId);

		tradeLicenseSearch.setAdminWardName(this.adminWardName);

		tradeLicenseSearch.setTradeAddress(this.tradeAddress);

		if (this.ownerShipType != null) {

			tradeLicenseSearch.setOwnerShipType(OwnerShipType.valueOf(this.ownerShipType));
		}

		tradeLicenseSearch.setTradeTitle(this.tradeTitle);

		if (this.tradeType != null) {

			tradeLicenseSearch.setTradeType(BusinessNature.valueOf(this.tradeType));
		}

		tradeLicenseSearch.setCategoryId(this.categoryId);

		tradeLicenseSearch.setCategory(this.category);

		tradeLicenseSearch.setSubCategoryId(this.subCategoryId);

		tradeLicenseSearch.setSubCategory(this.subCategory);

		tradeLicenseSearch.setUomId(this.uomId);

		tradeLicenseSearch.setUom(this.uom);

		tradeLicenseSearch.setQuantity(this.quantity);

		tradeLicenseSearch.setValidityYears(this.validityYears);

		tradeLicenseSearch.setRemarks(this.remarks);

		if (this.tradeCommencementDate != null) {

			tradeLicenseSearch.setTradeCommencementDate((this.tradeCommencementDate.getTime()));
		}

		if (this.agreementDate != null) {

			tradeLicenseSearch.setAgreementDate((this.agreementDate.getTime()));
		}

		if (this.licenseValidFromDate != null) {

			tradeLicenseSearch.setLicenseValidFromDate((this.licenseValidFromDate.getTime()));
		}

		tradeLicenseSearch.setIsPropertyOwner(this.isPropertyOwner);

		tradeLicenseSearch.setAgreementNo(this.agreementNo);

		tradeLicenseSearch.setIsLegacy(this.isLegacy);

		tradeLicenseSearch.setActive(this.active);

		tradeLicenseSearch.setStatus(this.status);

		tradeLicenseSearch.setStatusName(this.statusName);

		if (this.expiryDate != null) {

			tradeLicenseSearch.setExpiryDate((this.expiryDate.getTime()));
		}

		this.feeDetails = new ArrayList<LicenseFeeDetailSearch>();

		if (this.feeDetailEntitys != null) {

			for (LicenseFeeDetailSearchEntity feeDetailEntity : this.feeDetailEntitys) {

				this.feeDetails.add(feeDetailEntity.toDomain());
			}
		}

		tradeLicenseSearch.setFeeDetails(this.feeDetails);

		this.supportDocuments = new ArrayList<SupportDocumentSearch>();

		if (this.supportDocumentEntitys != null) {

			for (SupportDocumentSearchEntity supportDocumentEntity : this.supportDocumentEntitys) {

				this.supportDocuments.add(supportDocumentEntity.toDomain());
			}
		}

		tradeLicenseSearch.setSupportDocuments(this.supportDocuments);

		auditDetails.setCreatedBy(this.createdBy);

		auditDetails.setCreatedTime(this.createdTime);

		auditDetails.setLastModifiedBy(this.lastModifiedBy);

		auditDetails.setLastModifiedTime(this.lastModifiedTime);

		tradeLicenseSearch.setAuditDetails(auditDetails);

		return tradeLicenseSearch;
	}

	public TradeLicenseSearchEntity toEntity(TradeLicenseSearch tradeLicenseSearch) {

		AuditDetails auditDetails = tradeLicenseSearch.getAuditDetails();

		this.id = tradeLicenseSearch.getId();

		this.tenantId = tradeLicenseSearch.getTenantId();

		if (tradeLicenseSearch.getApplicationType() != null) {

			this.applicationType = tradeLicenseSearch.getApplicationType().toString();
		}

		this.applicationNumber = tradeLicenseSearch.getApplicationNumber();

		this.licenseNumber = tradeLicenseSearch.getLicenseNumber();

		this.oldLicenseNumber = tradeLicenseSearch.getOldLicenseNumber();

		if (tradeLicenseSearch.getApplicationDate() != null) {

			this.applicationDate = new Timestamp(tradeLicenseSearch.getApplicationDate());
		}

		this.adhaarNumber = tradeLicenseSearch.getAdhaarNumber();

		this.mobileNumber = tradeLicenseSearch.getMobileNumber();

		this.ownerName = tradeLicenseSearch.getOwnerName();

		this.fatherSpouseName = tradeLicenseSearch.getFatherSpouseName();

		this.emailId = tradeLicenseSearch.getEmailId();

		this.ownerAddress = tradeLicenseSearch.getOwnerAddress();

		this.propertyAssesmentNo = tradeLicenseSearch.getPropertyAssesmentNo();

		this.localityId = tradeLicenseSearch.getLocalityId();

		this.localityName = tradeLicenseSearch.getLocalityName();

		this.revenueWardId = tradeLicenseSearch.getRevenueWardId();

		this.revenueWardName = tradeLicenseSearch.getRevenueWardName();

		this.adminWardId = tradeLicenseSearch.getAdminWardId();

		this.adminWardName = tradeLicenseSearch.getAdminWardName();

		this.tradeAddress = tradeLicenseSearch.getTradeAddress();

		if (tradeLicenseSearch.getOwnerShipType() != null) {

			this.ownerShipType = tradeLicenseSearch.getOwnerShipType().toString();
		}

		this.tradeTitle = tradeLicenseSearch.getTradeTitle();

		if (tradeLicenseSearch.getTradeType() != null) {

			this.tradeType = tradeLicenseSearch.getTradeType().toString();
		}

		this.status = tradeLicenseSearch.getStatus();

		this.statusName = tradeLicenseSearch.getStatusName();

		this.categoryId = tradeLicenseSearch.getCategoryId();

		this.category = tradeLicenseSearch.getCategory();

		this.subCategoryId = tradeLicenseSearch.getSubCategoryId();

		this.subCategory = tradeLicenseSearch.getUom();

		this.uomId = tradeLicenseSearch.getUomId();

		this.uom = tradeLicenseSearch.getUom();

		this.quantity = tradeLicenseSearch.getQuantity();

		this.validityYears = tradeLicenseSearch.getValidityYears();

		this.remarks = tradeLicenseSearch.getRemarks();

		if (tradeLicenseSearch.getTradeCommencementDate() != null) {

			this.tradeCommencementDate = new Timestamp(tradeLicenseSearch.getTradeCommencementDate());
		}

		if (tradeLicenseSearch.getAgreementDate() != null) {

			this.agreementDate = new Timestamp(tradeLicenseSearch.getAgreementDate());
		}

		if (tradeLicenseSearch.getLicenseValidFromDate() != null) {

			this.licenseValidFromDate = new Timestamp(tradeLicenseSearch.getLicenseValidFromDate());
		}

		this.isPropertyOwner = tradeLicenseSearch.getIsPropertyOwner();

		this.agreementNo = tradeLicenseSearch.getAgreementNo();

		this.isLegacy = tradeLicenseSearch.getIsLegacy();

		this.active = tradeLicenseSearch.getActive();

		if (tradeLicenseSearch.getExpiryDate() != null) {

			this.expiryDate = new Timestamp(tradeLicenseSearch.getExpiryDate());
		}

		this.feeDetails = tradeLicenseSearch.getFeeDetails();

		this.supportDocuments = tradeLicenseSearch.getSupportDocuments();

		this.createdBy = (auditDetails == null) ? null : auditDetails.getCreatedBy();

		this.lastModifiedBy = (auditDetails == null) ? null : auditDetails.getLastModifiedBy();

		this.createdTime = (auditDetails == null) ? null : auditDetails.getCreatedTime();

		this.lastModifiedTime = (auditDetails == null) ? null : auditDetails.getLastModifiedTime();

		return this;
	}

}