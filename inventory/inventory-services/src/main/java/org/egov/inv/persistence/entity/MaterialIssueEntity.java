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

	public static final String TABLE_NAME = "materialissue";
	public static final String SEQUENCE_NAME = "seq_materialissue";
	public static final String ALIAS = "materialissue";

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

	private String indentNumber;

	private String issuedToEmployee;

	private String issuedToDesignation;

	private String issuePurpose;

	private String supplier;
	
	private Boolean scrapCreated;

	private Long stateId;

	private String createdBy;

	private Long createdTime;

	private String lastmodifiedBy;

	private Long lastModifiedTime;

	public Object toEntity(MaterialIssue materialIssue, String type) {
		if (materialIssue.getId() != null)
			this.id = materialIssue.getId();
		this.tenantId = materialIssue.getTenantId();
		if (materialIssue.getDescription() != null)
			this.description = materialIssue.getDescription();
		if (materialIssue.getDesignation() != null)
			this.designation = materialIssue.getDesignation();
		if (materialIssue.getIssueType() != null)
			this.issueType = materialIssue.getIssueType().name();
		if (materialIssue.getFromStore() != null && StringUtils.isNotBlank(materialIssue.getFromStore().getCode()))
			this.fromStore = materialIssue.getFromStore().getCode();
		if (materialIssue.getToStore() != null && StringUtils.isNotBlank(materialIssue.getToStore().getCode()))
			this.toStore = materialIssue.getToStore().getCode();
		this.issueNumber = materialIssue.getIssueNumber();
		this.issueDate = materialIssue.getIssueDate();
		if (materialIssue.getMaterialIssueStatus() != null)
			this.materialIssueStatus = materialIssue.getMaterialIssueStatus().name();
		if (materialIssue.getTotalIssueValue() != null)
			this.totalIssueValue = Double.valueOf(materialIssue.getTotalIssueValue().toString());
		if (materialIssue.getFileStoreId() != null)
			this.fileStoreId = materialIssue.getFileStoreId();
		if (materialIssue.getDesignation() != null)
			this.designation = materialIssue.getDesignation();
		if (type.equals(IssueTypeEnum.INDENTISSUE.toString())) {
			if (materialIssue.getIndent() != null && materialIssue.getIndent().getIndentNumber() != null)
				this.indentNumber = materialIssue.getIndent().getIndentNumber();
		}
		if (type.equals(IssueTypeEnum.INDENTISSUE.toString()))
			this.issuedToEmployee = materialIssue.getIndent().getIndentCreatedBy();
		else {
			if (materialIssue.getIssuedToEmployee() != null)
				this.issuedToEmployee = materialIssue.getIssuedToEmployee();
		}
		if (type.equals(IssueTypeEnum.INDENTISSUE))
			this.issuedToDesignation = materialIssue.getIndent().getDesignation();
		else {
			if (materialIssue.getIssuedToDesignation() != null)
				this.issuedToDesignation = materialIssue.getIssuedToDesignation();
		}
		if (materialIssue.getIssuePurpose() != null)
			this.issuePurpose = materialIssue.getIssuePurpose().name();
		if (materialIssue.getStateId() != null)
			this.stateId = materialIssue.getStateId();
		return this;
	}

	public MaterialIssue toDomain(String type) {
		MaterialIssue materialIssue = new MaterialIssue();
		materialIssue.setId(id);
		materialIssue.setTenantId(tenantId);
		materialIssue.setIssueType(IssueTypeEnum.valueOf(issueType));
		Store issueStore = new Store();
		issueStore.setCode(fromStore);
		Store indentStore = new Store();
		indentStore.setCode(toStore);
		if (null != type && (type.equals(IssueTypeEnum.INDENTISSUE.toString())
				|| type.equals(IssueTypeEnum.MATERIALOUTWARD.toString()))) {
			Indent indent = new Indent();
			indent.setIndentNumber(indentNumber);
			materialIssue.setIndent(indent);
		}
		materialIssue.setFromStore(issueStore);
		materialIssue.setToStore(indentStore);
		materialIssue.setIssueNumber(issueNumber);
		materialIssue.setIssueDate(issueDate);
		materialIssue.setMaterialIssueStatus(MaterialIssueStatusEnum.valueOf(materialIssueStatus));
		materialIssue.setDescription(description);
		materialIssue.setTotalIssueValue(BigDecimal.valueOf(totalIssueValue));
		materialIssue.setFileStoreId(fileStoreId);
		materialIssue.setDesignation(designation);
		materialIssue.setIssuedToEmployee(issuedToEmployee);
		materialIssue.setIssuedToDesignation(issuedToDesignation);
		materialIssue.setScrapCreated(scrapCreated);
		if (StringUtils.isNotBlank(issuePurpose))
			materialIssue.setIssuePurpose(IssuePurposeEnum.valueOf(issuePurpose));
		Supplier materialSupplier = new Supplier();
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
