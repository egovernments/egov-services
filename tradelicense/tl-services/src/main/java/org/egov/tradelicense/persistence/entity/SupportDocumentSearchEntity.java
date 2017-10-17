package org.egov.tradelicense.persistence.entity;

import org.egov.tradelicense.domain.model.AuditDetails;
import org.egov.tradelicense.domain.model.SupportDocumentSearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupportDocumentSearchEntity {

	public static final String TABLE_NAME = "egtl_support_document";
	public static final String SEQUENCE_NAME = "seq_egtl_support_document";

	private Long id;

	// private Long licenseId;

	private String tenantId;

	private Long documentTypeId;

	private Long applicationId;

	private String documentTypeName;

	private String fileStoreId;

	private String comments;

	private String createdBy;

	private String lastModifiedBy;

	private Long createdTime;

	private Long lastModifiedTime;

	public SupportDocumentSearch toDomain() {

		SupportDocumentSearch supportDocument = new SupportDocumentSearch();

		AuditDetails auditDetails = new AuditDetails();

		supportDocument.setId(this.id);

		// supportDocument.setLicenseId(this.licenseId);

		supportDocument.setTenantId(this.tenantId);

		supportDocument.setApplicationId(this.applicationId);

		supportDocument.setDocumentTypeId(this.documentTypeId);

		supportDocument.setFileStoreId(this.fileStoreId);

		supportDocument.setComments(this.comments);

		supportDocument.setDocumentTypeName(this.documentTypeName);

		auditDetails.setCreatedBy(this.createdBy);

		auditDetails.setCreatedTime(this.createdTime);

		auditDetails.setLastModifiedBy(this.lastModifiedBy);

		auditDetails.setLastModifiedTime(this.lastModifiedTime);

		supportDocument.setAuditDetails(auditDetails);

		return supportDocument;
	}

	public SupportDocumentSearchEntity toEntity(SupportDocumentSearch supportDocument) {

		AuditDetails auditDetails = supportDocument.getAuditDetails();

		this.id = supportDocument.getId();

		this.tenantId = supportDocument.getTenantId();

		this.applicationId = supportDocument.getApplicationId();

		this.documentTypeId = supportDocument.getDocumentTypeId();

		this.fileStoreId = supportDocument.getFileStoreId();

		this.documentTypeName = supportDocument.getDocumentTypeName();

		this.comments = supportDocument.getComments();

		this.createdBy = (auditDetails == null) ? null : auditDetails.getCreatedBy();

		this.lastModifiedBy = (auditDetails == null) ? null : auditDetails.getLastModifiedBy();

		this.createdTime = (auditDetails == null) ? null : auditDetails.getCreatedTime();

		this.lastModifiedTime = (auditDetails == null) ? null : auditDetails.getLastModifiedTime();

		return this;
	}
}
