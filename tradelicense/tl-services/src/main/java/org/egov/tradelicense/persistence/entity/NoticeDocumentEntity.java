package org.egov.tradelicense.persistence.entity;

import org.egov.tradelicense.domain.enums.DocumentName;
import org.egov.tradelicense.domain.model.AuditDetails;
import org.egov.tradelicense.domain.model.NoticeDocument;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDocumentEntity {

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

	public NoticeDocument toDomain() {

		NoticeDocument noticeDocument = new NoticeDocument();
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

		noticeDocument.setAuditDetails(auditDetails);

		return noticeDocument;
	}

	public NoticeDocumentEntity toEntity(NoticeDocument noticeDocument) {

		AuditDetails auditDetails = noticeDocument.getAuditDetails();

		this.tenantId = noticeDocument.getTenantId();

		this.setId(noticeDocument.getId());

		this.setLicenseId(noticeDocument.getLicenseId());

		this.setDocumentName(noticeDocument.getDocumentName().toString());

		this.setFileStoreId(noticeDocument.getFileStoreId());

		this.createdBy = (auditDetails == null) ? null : auditDetails.getCreatedBy();

		this.lastModifiedBy = (auditDetails == null) ? null : auditDetails.getLastModifiedBy();

		this.createdTime = (auditDetails.getCreatedTime() == null) ? System.currentTimeMillis()
				: auditDetails.getCreatedTime();

		this.lastModifiedTime = (auditDetails.getLastModifiedTime() == null) ? System.currentTimeMillis()
				: auditDetails.getLastModifiedTime();

		return this;
	}
}
