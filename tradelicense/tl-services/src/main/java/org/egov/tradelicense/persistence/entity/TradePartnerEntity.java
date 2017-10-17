package org.egov.tradelicense.persistence.entity;

import org.egov.tradelicense.domain.enums.Gender;
import org.egov.tradelicense.domain.model.AuditDetails;
import org.egov.tradelicense.domain.model.TradePartner;
import org.egov.tradelicense.domain.model.TradePartnerSearch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Builder
public class TradePartnerEntity {

	public static final String TABLE_NAME = "egtl_trade_partner";
	public static final String SEQUENCE_NAME = "seq_egtl_trade_partner";

	private Long id;

	private String tenantId;

	private Long licenseId;

	private String aadhaarNumber;

	private String fullName;

	private String gender;

	private String birthYear;

	private String emailId;

	private String designation;

	private String residentialAddress;

	private String correspondenceAddress;

	private String phoneNumber;

	private String mobileNumber;

	private String photo;

	private String createdBy;

	private String lastModifiedBy;

	private Long createdTime;

	private Long lastModifiedTime;

	public TradePartner toDomain() {

		TradePartner tradePartner = new TradePartner();

		AuditDetails auditDetails = new AuditDetails();

		tradePartner.setId(this.id);

		tradePartner.setTenantId(this.tenantId);

		tradePartner.setLicenseId(this.licenseId);

		tradePartner.setAadhaarNumber(this.aadhaarNumber);

		tradePartner.setFullName(this.fullName);

		if (this.gender != null && !this.gender.isEmpty()) {

			tradePartner.setGender(Gender.valueOf(this.gender));
		}

		tradePartner.setBirthYear(this.birthYear);

		tradePartner.setEmailId(this.emailId);

		tradePartner.setDesignation(this.designation);

		tradePartner.setResidentialAddress(this.residentialAddress);

		tradePartner.setCorrespondenceAddress(this.correspondenceAddress);

		tradePartner.setPhoneNumber(this.phoneNumber);

		tradePartner.setMobileNumber(this.mobileNumber);

		tradePartner.setPhoto(this.photo);

		auditDetails.setCreatedBy(this.createdBy);

		auditDetails.setCreatedTime(this.createdTime);

		auditDetails.setLastModifiedBy(this.lastModifiedBy);

		auditDetails.setLastModifiedTime(this.lastModifiedTime);

		tradePartner.setAuditDetails(auditDetails);

		return tradePartner;

	}

	public TradePartnerEntity toEntity(TradePartner tradePartner) {

		AuditDetails auditDetails = tradePartner.getAuditDetails();

		this.id = tradePartner.getId();

		this.tenantId = tradePartner.getTenantId();

		this.licenseId = tradePartner.getLicenseId();

		this.aadhaarNumber = tradePartner.getAadhaarNumber();

		this.fullName = tradePartner.getFullName();

		if (tradePartner.getGender() != null) {

			this.gender = tradePartner.getGender().toString();
		}

		this.birthYear = tradePartner.getBirthYear();

		this.emailId = tradePartner.getEmailId();

		this.designation = tradePartner.getDesignation();

		this.residentialAddress = tradePartner.getResidentialAddress();

		this.correspondenceAddress = tradePartner.getCorrespondenceAddress();

		this.phoneNumber = tradePartner.getPhoneNumber();

		this.mobileNumber = tradePartner.getMobileNumber();

		this.photo = tradePartner.getPhoto();

		this.createdBy = (auditDetails == null) ? null : auditDetails.getCreatedBy();

		this.lastModifiedBy = (auditDetails == null) ? null : auditDetails.getLastModifiedBy();

		this.createdTime = (auditDetails == null) ? null : auditDetails.getCreatedTime();

		this.lastModifiedTime = (auditDetails == null) ? null : auditDetails.getLastModifiedTime();

		return this;
	}

	public TradePartnerEntity toSearchEntity(TradePartnerSearch tradePartner) {

		this.id = tradePartner.getId();

		this.tenantId = tradePartner.getTenantId();

		this.licenseId = tradePartner.getLicenseId();

		return this;
	}
}