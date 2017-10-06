package org.egov.tl.masters.persistence.entity;

import org.egov.tl.commons.web.contract.AuditDetails;
import org.egov.tl.commons.web.contract.DocumentType;
import org.egov.tl.commons.web.contract.enums.ApplicationTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class describe the set of fields contained in a Trade license
 * DocumentType
 * 
 * @author Shubham pratap Singh
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentTypeEntity {

	public static final String TABLE_NAME = "egtl_mstr_document_type";
	public static final String SEQUENCE_NAME = "seq_egtl_mstr_document_type";

	private Long id = null;

	private String tenantId = null;

	private String name = null;

	private Boolean mandatory = true;

	private Boolean enabled = true;

	private String applicationType;

	private String category ;

	private String subCategory ;

	private String createdBy = null;

	private String lastModifiedBy = null;

	private Long createdTime = null;

	private Long lastModifiedTime = null;




	public DocumentType toDomain()
	{
		DocumentType documentType = new DocumentType();;
		AuditDetails auditDetails = new AuditDetails();
		documentType.setId(this.id);
		documentType.setApplicationType(( this.applicationType ==null ? null : ApplicationTypeEnum.fromValue(this.applicationType) ));
		documentType.setEnabled(this.enabled);
		documentType.setMandatory(this.mandatory);
		documentType.setName(this.name);
		documentType.setTenantId(this.tenantId);
		documentType.setCategory(this.category);
		documentType.setSubCategory(this.subCategory);
		auditDetails.setCreatedBy(this.createdBy);
		auditDetails.setCreatedTime(this.createdTime);
		auditDetails.setLastModifiedBy(this.lastModifiedBy);
		auditDetails.setLastModifiedTime(this.lastModifiedTime);
		documentType.setAuditDetails(auditDetails);
		return documentType;
	}

	public DocumentTypeEntity toEntity(DocumentType documentType)
	{
		this.id = documentType.getId();
		this.applicationType = (documentType.getApplicationType() == null ? null : documentType.getApplicationType().name());
		this.name = documentType.getName();
		this.tenantId = documentType.getTenantId();
		this.subCategory = documentType.getSubCategory();
		this.category = documentType.getCategory();
		this.enabled = documentType.getEnabled();
		this.mandatory = documentType.getMandatory();
		this.createdBy = (documentType.getAuditDetails()) == null ? null : documentType.getAuditDetails().getCreatedBy();
		this.lastModifiedBy = (documentType.getAuditDetails()) == null ? null : documentType.getAuditDetails().getLastModifiedBy();
		this.lastModifiedTime = (documentType.getAuditDetails()) == null ? null : documentType.getAuditDetails().getLastModifiedTime();
		this.lastModifiedBy = (documentType.getAuditDetails()) == null ? null : documentType.getAuditDetails().getLastModifiedBy();

		return this;
	}
}