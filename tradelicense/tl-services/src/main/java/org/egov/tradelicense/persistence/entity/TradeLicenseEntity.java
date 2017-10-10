package org.egov.tradelicense.persistence.entity;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.egov.tradelicense.domain.enums.ApplicationType;
import org.egov.tradelicense.domain.enums.BusinessNature;
import org.egov.tradelicense.domain.enums.OwnerShipType;
import org.egov.tradelicense.domain.model.AuditDetails;
import org.egov.tradelicense.domain.model.LicenseFeeDetail;
import org.egov.tradelicense.domain.model.SupportDocument;
import org.egov.tradelicense.domain.model.TradeLicense;
import org.postgresql.util.PGobject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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

	private String licenseNumber;

	private String oldLicenseNumber;

	protected Timestamp applicationDate;

	private String adhaarNumber;

	private String mobileNumber;

	private String ownerName;

	private String fatherSpouseName;

	private String emailId;

	private String ownerAddress;

	private String propertyAssesmentNo;

	private Integer localityId;

	private Integer revenueWardId;

	private Integer adminWardId;

	private String tradeAddress;

	private String ownerShipType;

	private String tradeTitle;

	private String tradeType;

	private String category;

	private String subCategory;

	private String uom;

	private Double quantity;

	private Long validityYears;

	private String remarks;

	private Timestamp tradeCommencementDate;

	private Timestamp issuedDate;

	private Timestamp agreementDate;

	private Timestamp licenseValidFromDate;

	private String status;

	private String agreementNo;

	private Boolean isLegacy = false;

	private Boolean isPropertyOwner = false;

	private Boolean active = true;

	private Timestamp expiryDate;

	private static List<LicenseFeeDetail> feeDetails;

	private static List<SupportDocument> supportDocuments;
	
	private String createdBy;

	private String lastModifiedBy;

	private Long createdTime;

	private Long lastModifiedTime;
	
	private PGobject licenseData;

	public TradeLicense toDomain() {

		TradeLicense tradeLicense = new TradeLicense();

		AuditDetails auditDetails = new AuditDetails();

		tradeLicense.setId(this.id);

		tradeLicense.setTenantId(this.tenantId);

		if (this.applicationType != null) {

			tradeLicense.setApplicationType(ApplicationType.valueOf(this.applicationType));
		}

		tradeLicense.setApplicationNumber(this.applicationNumber);

		tradeLicense.setLicenseNumber(this.licenseNumber);

		tradeLicense.setOldLicenseNumber(this.oldLicenseNumber);

		if (this.applicationDate != null) {

			tradeLicense.setApplicationDate((this.applicationDate.getTime()));
		}

		tradeLicense.setAdhaarNumber(this.adhaarNumber);

		tradeLicense.setMobileNumber(this.mobileNumber);

		tradeLicense.setOwnerName(this.ownerName);

		tradeLicense.setFatherSpouseName(this.fatherSpouseName);

		tradeLicense.setEmailId(this.emailId);

		tradeLicense.setOwnerAddress(this.ownerAddress);

		tradeLicense.setPropertyAssesmentNo(this.propertyAssesmentNo);

		tradeLicense.setLocalityId(this.localityId);

		tradeLicense.setRevenueWardId(this.revenueWardId);

		tradeLicense.setAdminWardId(this.adminWardId);

		tradeLicense.setTradeAddress(this.tradeAddress);

		if (this.ownerShipType != null) {

			tradeLicense.setOwnerShipType(OwnerShipType.valueOf(this.ownerShipType));
		}

		tradeLicense.setTradeTitle(this.tradeTitle);

		if (this.tradeType != null) {

			tradeLicense.setTradeType(BusinessNature.valueOf(this.tradeType));
		}

		tradeLicense.setCategory(this.category);

		tradeLicense.setSubCategory(this.subCategory);

		tradeLicense.setUom(this.uom);

		tradeLicense.setQuantity(this.quantity);

		tradeLicense.setValidityYears(this.validityYears);

		tradeLicense.setRemarks(this.remarks);

		if (this.tradeCommencementDate != null) {

			tradeLicense.setTradeCommencementDate((this.tradeCommencementDate.getTime()));
		}

		if (this.agreementDate != null) {

			tradeLicense.setAgreementDate((this.agreementDate.getTime()));
		}

		if (this.licenseValidFromDate != null) {

			tradeLicense.setLicenseValidFromDate((this.licenseValidFromDate.getTime()));
		}

		if (this.issuedDate != null) {

			tradeLicense.setIssuedDate((this.issuedDate.getTime()));
		}

		tradeLicense.setAgreementNo(this.agreementNo);

		tradeLicense.setIsLegacy(this.isLegacy);

		tradeLicense.setIsPropertyOwner(this.isPropertyOwner);

		tradeLicense.setActive(this.active);

		tradeLicense.setStatus(this.status);

		if (this.expiryDate != null) {

			tradeLicense.setExpiryDate((this.expiryDate.getTime()));
		}

		tradeLicense.setFeeDetails(this.feeDetails);

		tradeLicense.setSupportDocuments(this.supportDocuments);
		
		if(this.licenseData != null && this.isLegacy != null && (this.isLegacy == Boolean.TRUE)){
			Gson gson = new GsonBuilder().serializeNulls().create();
			tradeLicense.setLicenseData(gson.fromJson(this.licenseData.toString(), Map.class));
		}
		
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

		this.licenseNumber = tradeLicense.getLicenseNumber();

		this.oldLicenseNumber = tradeLicense.getOldLicenseNumber();

		if (tradeLicense.getApplicationDate() != null) {

			this.applicationDate = new Timestamp(tradeLicense.getApplicationDate());
		}

		this.adhaarNumber = tradeLicense.getAdhaarNumber();

		this.mobileNumber = tradeLicense.getMobileNumber();

		this.ownerName = tradeLicense.getOwnerName();

		this.fatherSpouseName = tradeLicense.getFatherSpouseName();

		this.emailId = tradeLicense.getEmailId();

		this.ownerAddress = tradeLicense.getOwnerAddress();

		this.propertyAssesmentNo = tradeLicense.getPropertyAssesmentNo();

		this.localityId = tradeLicense.getLocalityId();

		this.revenueWardId = tradeLicense.getRevenueWardId();

		this.adminWardId = tradeLicense.getAdminWardId();

		this.tradeAddress = tradeLicense.getTradeAddress();

		if (tradeLicense.getOwnerShipType() != null) {

			this.ownerShipType = tradeLicense.getOwnerShipType().toString();
		}

		this.tradeTitle = tradeLicense.getTradeTitle();

		if (tradeLicense.getTradeType() != null) {

			this.tradeType = tradeLicense.getTradeType().toString();
		}

		this.status = tradeLicense.getStatus();

		this.category = tradeLicense.getCategory();

		this.subCategory = tradeLicense.getSubCategory();

		this.uom = tradeLicense.getUom();

		this.quantity = tradeLicense.getQuantity();

		this.validityYears = tradeLicense.getValidityYears();

		this.remarks = tradeLicense.getRemarks();

		if (tradeLicense.getTradeCommencementDate() != null) {

			this.tradeCommencementDate = new Timestamp(tradeLicense.getTradeCommencementDate());
		}

		if (tradeLicense.getIssuedDate() != null) {

			this.issuedDate = new Timestamp(tradeLicense.getIssuedDate());
		}

		if (tradeLicense.getAgreementDate() != null) {

			this.agreementDate = new Timestamp(tradeLicense.getAgreementDate());
		}

		if (tradeLicense.getLicenseValidFromDate() != null) {

			this.licenseValidFromDate = new Timestamp(tradeLicense.getLicenseValidFromDate());
		}

		this.agreementNo = tradeLicense.getAgreementNo();

		this.isLegacy = tradeLicense.getIsLegacy();

		this.isPropertyOwner = tradeLicense.getIsPropertyOwner();

		this.active = tradeLicense.getActive();

		if (tradeLicense.getExpiryDate() != null) {

			this.expiryDate = new Timestamp(tradeLicense.getExpiryDate());
		}

		this.feeDetails = tradeLicense.getFeeDetails();

		this.supportDocuments = tradeLicense.getSupportDocuments();
		
		if(tradeLicense.getLicenseData() != null && !tradeLicense.getLicenseData().isEmpty() && tradeLicense.getIsLegacy()){
			
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
		
		
		this.createdBy = (auditDetails == null) ? null : auditDetails.getCreatedBy();

		this.lastModifiedBy = (auditDetails == null) ? null : auditDetails.getLastModifiedBy();

		this.createdTime = (auditDetails == null) ? null : auditDetails.getCreatedTime();

		this.lastModifiedTime = (auditDetails == null) ? null : auditDetails.getLastModifiedTime();

		return this;
	}

}
