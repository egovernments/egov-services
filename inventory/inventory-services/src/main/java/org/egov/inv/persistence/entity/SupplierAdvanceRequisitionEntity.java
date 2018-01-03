package org.egov.inv.persistence.entity;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.egov.inv.model.AuditDetails;
import org.egov.inv.model.PurchaseOrder;
import org.egov.inv.model.Supplier;
import org.egov.inv.model.SupplierAdvanceRequisition;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierAdvanceRequisitionEntity {

    private String id;

    private String tenantId;

    private String supplier;

    private String purchaseOrder;

    private BigDecimal advanceAdjustedAmount;

    private Boolean advanceFullyAdjustedInBill;

    private String stateId;

    private String sarStatus;

    private String createdBy;

    private Long createdTime;

    private String lastModifiedBy;

    private Long lastModifiedTime;

	public SupplierAdvanceRequisition toDomain() {
		SupplierAdvanceRequisition supplierAdvanceRequisition = new SupplierAdvanceRequisition();
		supplierAdvanceRequisition.setId(this.id);
		supplierAdvanceRequisition.setTenantId(this.tenantId);
		supplierAdvanceRequisition.setSarStatus(this.sarStatus);
		supplierAdvanceRequisition.setStateId(this.stateId);
		supplierAdvanceRequisition.setSupplier(new Supplier().code(supplier));
		supplierAdvanceRequisition.setPurchaseOrder(new PurchaseOrder().purchaseOrderNumber(purchaseOrder));
		supplierAdvanceRequisition.setAdvanceAdjustedAmount(this.advanceAdjustedAmount);
		supplierAdvanceRequisition.setAdvanceFullyAdjustedInBill(this.advanceFullyAdjustedInBill);
		AuditDetails auditDetail = new AuditDetails()
				.createdBy(createdBy)
				.lastModifiedBy(lastModifiedBy)
				.createdTime(createdTime)
				.lastModifiedTime(lastModifiedTime);
		supplierAdvanceRequisition.setAuditDetails(auditDetail);
		return supplierAdvanceRequisition;
	}

	public SupplierAdvanceRequisitionEntity toEntity(SupplierAdvanceRequisition supplierAdvanceRequisition) {
		this.id = supplierAdvanceRequisition.getId();
		this.tenantId = supplierAdvanceRequisition.getTenantId();
		this.sarStatus = supplierAdvanceRequisition.getSarStatus();
		this.stateId = supplierAdvanceRequisition.getStateId();
		this.supplier = supplierAdvanceRequisition.getSupplier() != null ? supplierAdvanceRequisition.getSupplier().getCode() : null;
		this.purchaseOrder = supplierAdvanceRequisition.getPurchaseOrder() != null ? supplierAdvanceRequisition.getPurchaseOrder().toString() : null;
		this.advanceAdjustedAmount = supplierAdvanceRequisition.getAdvanceAdjustedAmount();
		this.advanceFullyAdjustedInBill = supplierAdvanceRequisition.getAdvanceFullyAdjustedInBill();
		this.createdBy=supplierAdvanceRequisition.getAuditDetails().getCreatedBy();
		this.lastModifiedBy=supplierAdvanceRequisition.getAuditDetails().getLastModifiedBy();
		this.createdTime=supplierAdvanceRequisition.getAuditDetails().getCreatedTime();
		this.lastModifiedTime=supplierAdvanceRequisition.getAuditDetails().getLastModifiedTime();
		
		return this;
	}
}
