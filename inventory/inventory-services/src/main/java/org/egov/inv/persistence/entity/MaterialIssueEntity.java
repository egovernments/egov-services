package org.egov.inv.persistence.entity;

import java.math.BigDecimal;

import org.egov.inv.model.MaterialIssue;
import org.egov.inv.model.MaterialIssue.IssueTypeEnum;

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

	private String issuetype;

	private String issuingstore;

	private String issueNoteNumber;

	private Long issueDate;

	private String materialIssueStatus;

	private String description;

	private Double totalIssueValue;

	private String fileStoreId;

	private String designation;

	private String indent;

	private String issuedtoemployee;

	private String issuedtodesignation;

	private String issuepurpose;

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
		this.issuetype = materialIssue.getIssueType().name();
		this.issuingstore = materialIssue.getToStore().getCode();
		this.issueNoteNumber = materialIssue.getIssueNumber();
		this.issueDate = materialIssue.getIssueDate();
		this.materialIssueStatus = materialIssue.getMaterialIssueStatus().name();
		this.totalIssueValue = Double.valueOf(materialIssue.getTotalIssueValue().toString());
		if(materialIssue.getFileStoreId() != null)
		this.fileStoreId = materialIssue.getFileStoreId();
		if(materialIssue.getDesignation() != null)
		this.designation = materialIssue.getDesignation();
		if(materialIssue.getIndent() != null && materialIssue.getIndent().getId() != null)
		this.indent = materialIssue.getIndent().getId();
		if(materialIssue.getIssuedToEmployee() != null)
		this.issuedtoemployee = materialIssue.getIssuedToEmployee();
		if(materialIssue.getIssuedToDesignation() != null)
		this.issuedtodesignation = materialIssue.getIssuedToDesignation();
		if(materialIssue.getIssuePurpose() != null)
		this.issuepurpose = materialIssue.getIssuePurpose().name();
		if(materialIssue.getStateId() != null)
		this.stateId = materialIssue.getStateId();

		return this;
	}
	
}
