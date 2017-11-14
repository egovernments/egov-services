package org.egov.inv.persistence.entity;

import java.math.BigDecimal;

import org.egov.inv.model.AuditDetails;
import org.egov.inv.model.Department;
import org.egov.inv.model.Indent;
import org.egov.inv.model.Indent.IndentPurposeEnum;
import org.egov.inv.model.Indent.IndentStatusEnum;
import org.egov.inv.model.Indent.IndentTypeEnum;
import org.egov.inv.model.Indent.InventoryTypeEnum;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.egov.inv.model.Store;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Builder
public class IndentEntity {
	public static final String TABLE_NAME = "indent";
	private String id;
	private String tenantId;
	private String issueStore;
	private String indentStore;
	private Long indentDate;
	private String indentNumber;
	private String indentType;
	private String indentPurpose;
	private String inventoryType;
	private Long expectedDeliveryDate;
	private String materialHandOverTo;
	private String narration;
	private String indentStatus;
	private String departmentId;
	private BigDecimal totalIndentValue;
	private String fileStoreId;
	private String indentCreatedBy;
	private String designation;
	private Long stateId;
	private String createdBy;
	private String lastModifiedBy;
	private Long createdTime;
	private Long lastModifiedTime;

	public Indent toDomain() {
		Indent indent = new Indent();
		// super.toDomain(indent);
		indent.setId(this.id);
		indent.setTenantId(this.tenantId);
		indent.setIssueStore(new Store().code(issueStore));
		indent.setIndentStore(new Store().code(indentStore));
		indent.setIndentDate(this.indentDate);
		indent.setIndentNumber(this.indentNumber);
		indent.setIndentType(IndentTypeEnum.fromValue(this.indentType));
		indent.setIndentPurpose(IndentPurposeEnum.fromValue(this.indentPurpose));
		indent.setInventoryType(InventoryTypeEnum.fromValue(this.inventoryType));
		indent.setExpectedDeliveryDate(this.expectedDeliveryDate);
		indent.setMaterialHandOverTo(this.materialHandOverTo);
		indent.setNarration(this.narration);
		if(indentStatus!=null)
		indent.setIndentStatus(IndentStatusEnum.fromValue(this.indentStatus));
		indent.setDepartment(new Department().code(departmentId));
		indent.setTotalIndentValue(this.totalIndentValue);
		indent.setFileStoreId(this.fileStoreId);
		indent.setIndentCreatedBy(this.indentCreatedBy);
		indent.setDesignation(this.designation);
		indent.setStateId(this.stateId);
		AuditDetails auditDetail = new AuditDetails()
				.createdBy(createdBy)
				.lastModifiedBy(lastModifiedBy)
				.createdTime(createdTime)
				.lastModifiedTime(lastModifiedTime);
		indent.setAuditDetails(auditDetail);
		return indent;
	}

	public IndentEntity toEntity(Indent indent) {
		// super.toEntity((Object) indent);
		this.id = indent.getId();
		this.tenantId = indent.getTenantId();
		this.issueStore = indent.getIssueStore() != null ? indent.getIssueStore().getCode() : null;
		this.indentStore = indent.getIndentStore() != null ? indent.getIndentStore().getCode() : null;
		this.indentDate = indent.getIndentDate();
		this.indentNumber = indent.getIndentNumber();
		this.indentType = indent.getIndentType() != null ? indent.getIndentType().toString() : null;
		this.indentPurpose = indent.getIndentPurpose() != null ? indent.getIndentPurpose().toString() : null;
		this.inventoryType = indent.getInventoryType() != null ? indent.getInventoryType().toString() : null;
		this.expectedDeliveryDate = indent.getExpectedDeliveryDate();
		this.materialHandOverTo = indent.getMaterialHandOverTo();
		this.narration = indent.getNarration();
		this.indentStatus = indent.getIndentStatus() != null ? indent.getIndentStatus().toString() : null;
		this.departmentId = indent.getDepartment() != null ? indent.getDepartment().getCode() : null;
		this.totalIndentValue = indent.getTotalIndentValue();
		this.fileStoreId = indent.getFileStoreId();
		this.indentCreatedBy = indent.getIndentCreatedBy();
		this.designation = indent.getDesignation();
		this.stateId = indent.getStateId();
		this.createdBy=indent.getAuditDetails().getCreatedBy();
		this.lastModifiedBy=indent.getAuditDetails().getLastModifiedBy();
		this.createdTime=indent.getAuditDetails().getCreatedTime();
		this.lastModifiedTime=indent.getAuditDetails().getLastModifiedTime();
		
		return this;
	}

}
