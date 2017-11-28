package org.egov.inv.persistence.entity;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.egov.inv.model.AuditDetails;
import org.egov.inv.model.Indent;
import org.egov.inv.model.MaterialIssue;
import org.egov.inv.model.MaterialIssue.IssuePurposeEnum;
import org.egov.inv.model.MaterialIssue.IssueTypeEnum;
import org.egov.inv.model.MaterialIssue.MaterialIssueStatusEnum;
import org.egov.inv.model.Store;
import org.egov.inv.model.Supplier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MaterialIssueEntity {

	private String id;

	private String tenantId;

	private String issueType;

	private String fromStore;
	
	private String toStore;

	private String issueNumber;

	private Long issueDate;

	private String materialIssueStatus;

	private String description;

	private Double totalIssueValue;

	private String fileStoreId;

	private String designation;

	private String indentId;

	private String issuedToEmployee;

	private String issuedToDesignation;

	private String issuePurpose;
	
	private String supplier;

	private Long stateId;

	private String createdBy;

	private Long createdTime;

	private String lastmodifiedBy;

	private Long lastModifiedTime;
	
	
	
	public Object toEntity(MaterialIssue materialIssue) {
		if(materialIssue.getId() != null)
		this.id = materialIssue.getId();
		this.tenantId = materialIssue.getTenantId();
		if(materialIssue.getDescription() != null)
		this.description = materialIssue.getDescription();
		if(materialIssue.getDesignation() != null)
		this.designation = materialIssue.getDesignation();
		this.issueType = materialIssue.getIssueType().name();
		this.fromStore = materialIssue.getFromStore().getCode();
		if(materialIssue.getToStore() != null)
		this.toStore = materialIssue.getToStore().getCode();
		this.issueNumber = materialIssue.getIssueNumber();
		this.issueDate = materialIssue.getIssueDate();
		this.materialIssueStatus = materialIssue.getMaterialIssueStatus().name();
		this.totalIssueValue = Double.valueOf(materialIssue.getTotalIssueValue().toString());
		if(materialIssue.getFileStoreId() != null)
		this.fileStoreId = materialIssue.getFileStoreId();
		if(materialIssue.getDesignation() != null)
		this.designation = materialIssue.getDesignation();
		if(materialIssue.getIndent() != null && materialIssue.getIndent().getId() != null)
		this.indentId = materialIssue.getIndent().getId();
		if(materialIssue.getIssuedToEmployee() != null)
		this.issuedToEmployee = materialIssue.getIssuedToEmployee();
		if(materialIssue.getIssuedToDesignation() != null)
		this.issuedToDesignation = materialIssue.getIssuedToDesignation();
		if(materialIssue.getIssuePurpose() != null)
		this.issuePurpose = materialIssue.getIssuePurpose().name();
		if(materialIssue.getStateId() != null)
		this.stateId = materialIssue.getStateId();
		return this;
	}



	public MaterialIssue toDomain() {
		MaterialIssue materialIssue = new MaterialIssue();
		materialIssue.setId(id);
		materialIssue.setTenantId(tenantId);
		materialIssue.setIssueType(IssueTypeEnum.valueOf(issueType));
		Store fromMaterialStore = new Store();
		fromMaterialStore.setCode(fromStore);
		materialIssue.setFromStore(fromMaterialStore);
		Store toMaterialStore = new Store();
		toMaterialStore.setCode(toStore);
		materialIssue.setToStore(toMaterialStore);
		materialIssue.setIssueNumber(issueNumber);
		materialIssue.setIssueDate(issueDate);
		materialIssue.setMaterialIssueStatus(MaterialIssueStatusEnum.valueOf(materialIssueStatus));
	    materialIssue.setDescription(description);	
	    materialIssue.setTotalIssueValue(BigDecimal.valueOf(totalIssueValue));
	    materialIssue.setFileStoreId(fileStoreId);
	    materialIssue.setDesignation(designation);
	    Indent indent = new Indent();
	    indent.setId(indentId);
	    materialIssue.setIndent(indent);
	    materialIssue.setIssuedToEmployee(issuedToEmployee);
	    materialIssue.setIssuedToDesignation(issuedToDesignation);
	    if(StringUtils.isNotBlank(issuePurpose))
	    materialIssue.setIssuePurpose(IssuePurposeEnum.valueOf(issuePurpose));
	    Supplier materialSupplier = new Supplier ();
	    materialSupplier.setCode(supplier);
	    materialIssue.setSupplier(materialSupplier);
	    materialIssue.setStateId(stateId);
	    AuditDetails auditDetails = new AuditDetails();
	    auditDetails.setCreatedBy(createdBy);
	    auditDetails.setCreatedTime(createdTime);
	    auditDetails.setLastModifiedBy(lastmodifiedBy);
	    auditDetails.setLastModifiedTime(lastModifiedTime);
	    materialIssue.setAuditDetails(auditDetails);
		return materialIssue;
	}
	
}
