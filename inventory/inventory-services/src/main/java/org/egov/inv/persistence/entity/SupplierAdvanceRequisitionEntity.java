package org.egov.inv.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.egov.inv.model.AuditDetails;
import org.egov.inv.model.PurchaseOrder;
import org.egov.inv.model.Supplier;
import org.egov.inv.model.SupplierAdvanceRequisition;

import java.math.BigDecimal;

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

    private String status;

    private String createdBy;

    private Long createdTime;

    private String lastmodifiedBy;

    private Long lastmodifiedTime;


    public SupplierAdvanceRequisition toDomain() {
        AuditDetails auditDetails = new AuditDetails();
        auditDetails.createdBy(createdBy)
                .createdTime(createdTime)
                .lastModifiedBy(lastmodifiedBy)
                .lastModifiedTime(lastmodifiedTime);

        SupplierAdvanceRequisition supplierAdvanceRequisition = new SupplierAdvanceRequisition();
        return supplierAdvanceRequisition.id(id)
                .tenantId(tenantId)
                .supplier(new Supplier().code(supplier))
                .purchaseOrder(new PurchaseOrder().id(purchaseOrder))
                .advanceAdjustedAmount(advanceAdjustedAmount)
                .advanceFullyAdjustedInBill(advanceFullyAdjustedInBill)
                .stateId(stateId)
                .sarStatus(status)
                .auditDetails(auditDetails);
    }
}
