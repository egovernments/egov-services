package org.egov.tradelicense.persistence.entity;

import java.sql.Timestamp;

import org.egov.tl.commons.web.contract.enums.ApplicationTypeEnum;
import org.egov.tradelicense.domain.enums.DocumentName;
import org.egov.tradelicense.domain.model.AuditDetails;
import org.egov.tradelicense.domain.model.NoticeDocumentSearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDocumentSearchEntity {

	public static final String TABLE_NAME = "egtl_notice_document";
	public static final String SEQUENCE_NAME = "seq_egtl_notice_document";

	private Long id;

	private Long licenseId;

	private String tenantId;

	private String documentName;

	private String fileStoreId;

	private String createdBy;

	private String lastModifiedBy;

	private Long createdTime;

	private Long lastModifiedTime;

	private String applicationNumber;

	private String tradeLicenseNumber;

	private String applicationType;

	private Timestamp applicationDate;

	private String validityYear;

	private String ward;

	private String status;

	private String ownerName;

	private String mobileNumber;

	private String tradeTitle;

	public NoticeDocumentSearch toDomain() {

		NoticeDocumentSearch noticeDocument = new NoticeDocumentSearch();
		AuditDetails auditDetails = new AuditDetails();

		noticeDocument.setId(this.id);

		noticeDocument.setTenantId(this.tenantId);

		noticeDocument.setLicenseId(this.licenseId);

		noticeDocument.setDocumentName(Enum.valueOf(DocumentName.class, this.documentName));

		noticeDocument.setFileStoreId(this.fileStoreId);

		auditDetails.setCreatedBy(this.createdBy);

		auditDetails.setCreatedTime(this.createdTime);

		auditDetails.setLastModifiedBy(this.lastModifiedBy);

		auditDetails.setLastModifiedTime(this.lastModifiedTime);

		if (this.applicationDate != null) {

			noticeDocument.setApplicationDate(this.applicationDate.getTime());
		}

		noticeDocument.setApplicationNumber(this.applicationNumber);

		noticeDocument.setTradeLicenseNumber(this.tradeLicenseNumber);

		noticeDocument.setApplicationType(
				this.applicationType == null ? null : ApplicationTypeEnum.fromValue(this.applicationType));

		noticeDocument.setValidityYear(this.validityYear);

		noticeDocument.setWard(this.ward);

		noticeDocument.setStatus(this.status);

		noticeDocument.setOwnerName(this.ownerName);

		noticeDocument.setMobileNumber(this.mobileNumber);

		noticeDocument.setTradeTitle(this.tradeTitle);

		noticeDocument.setAuditDetails(auditDetails);

		return noticeDocument;
	}

	public NoticeDocumentSearchEntity toEntity(NoticeDocumentSearch noticeDocument) {

		AuditDetails auditDetails = noticeDocument.getAuditDetails();

		this.tenantId = noticeDocument.getTenantId();

		this.setId(noticeDocument.getId());

		this.setLicenseId(noticeDocument.getLicenseId());

		this.setDocumentName(noticeDocument.getDocumentName().toString());

		this.setFileStoreId(noticeDocument.getFileStoreId());

		if (noticeDocument.getApplicationDate() != null) {

			this.setApplicationDate(new Timestamp(noticeDocument.getApplicationDate()));
		}

		this.setValidityYear(noticeDocument.getValidityYear());

		this.setApplicationType(
				noticeDocument.getApplicationType() == null ? null : noticeDocument.getApplicationType().toString());

		this.setApplicationNumber(noticeDocument.getApplicationNumber());

		this.setMobileNumber(noticeDocument.getMobileNumber());

		this.setStatus(noticeDocument.getStatus());

		this.setOwnerName(noticeDocument.getOwnerName());

		this.setMobileNumber(this.mobileNumber);

		this.setTradeTitle(this.tradeTitle);

		this.createdBy = (auditDetails == null) ? null : auditDetails.getCreatedBy();

		this.lastModifiedBy = (auditDetails == null) ? null : auditDetails.getLastModifiedBy();

		this.createdTime = (auditDetails.getCreatedTime() == null) ? System.currentTimeMillis()
				: auditDetails.getCreatedTime();

		this.lastModifiedTime = (auditDetails.getLastModifiedTime() == null) ? System.currentTimeMillis()
				: auditDetails.getLastModifiedTime();

		return this;
	}
}
