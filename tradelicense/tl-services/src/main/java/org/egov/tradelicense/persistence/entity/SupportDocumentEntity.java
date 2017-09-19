package org.egov.tradelicense.persistence.entity;

import org.egov.tradelicense.domain.model.AuditDetails;
import org.egov.tradelicense.domain.model.SupportDocument;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Builder
public class SupportDocumentEntity {

	public static final String TABLE_NAME = "egtl_support_document";
	public static final String SEQUENCE_NAME = "seq_egtl_support_document";

	private Long id;

	private Long documentTypeId;

	private String tenantId;

	private String fileStoreId;

	private String comments;

	private String createdBy;

	private String lastModifiedBy;

	private Long createdTime;

	private Long lastModifiedTime;

	private Long applicationId;

	public SupportDocument toDomain() {

		SupportDocument supportDocument = new SupportDocument();

		AuditDetails auditDetails = new AuditDetails();

		supportDocument.setId(this.id);

		supportDocument.setApplicationId(this.applicationId);

		supportDocument.setTenantId(this.tenantId);

		supportDocument.setDocumentTypeId(this.documentTypeId);

		supportDocument.setFileStoreId(this.fileStoreId);

		supportDocument.setComments(this.comments);

		auditDetails.setCreatedBy(this.createdBy);

		auditDetails.setCreatedTime(this.createdTime);

		auditDetails.setLastModifiedBy(this.lastModifiedBy);

		auditDetails.setLastModifiedTime(this.lastModifiedTime);

		supportDocument.setAuditDetails(auditDetails);

		return supportDocument;
	}

	public SupportDocumentEntity toEntity(SupportDocument supportDocument) {

		AuditDetails auditDetails = supportDocument.getAuditDetails();

		this.id = supportDocument.getId();

		this.tenantId = supportDocument.getTenantId();

		this.applicationId = supportDocument.getApplicationId();

		this.documentTypeId = supportDocument.getDocumentTypeId();

		this.fileStoreId = supportDocument.getFileStoreId();

		this.comments = supportDocument.getComments();

		this.createdBy = (auditDetails == null) ? null : auditDetails.getCreatedBy();

		this.lastModifiedBy = (auditDetails == null) ? null : auditDetails.getLastModifiedBy();

		this.createdTime = (auditDetails == null) ? null : auditDetails.getCreatedTime();

		this.lastModifiedTime = (auditDetails == null) ? null : auditDetails.getLastModifiedTime();

		return this;
	}
}
