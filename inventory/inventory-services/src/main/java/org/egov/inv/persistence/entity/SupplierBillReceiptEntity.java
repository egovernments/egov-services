package org.egov.inv.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.egov.inv.model.AuditDetails;
import org.egov.inv.model.MaterialReceipt;
import org.egov.inv.model.SupplierBillReceipt;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierBillReceiptEntity {

    private String id;

    private String tenantId;

    private String supplierBill;

    private String materialReceipt;

    private String createdBy;

    private Long createdTime;

    private String lastmodifiedBy;

    private Long lastmodifiedTime;

    public SupplierBillReceipt toDomain() {

        AuditDetails auditDetails = new AuditDetails();
        auditDetails.createdBy(createdBy)
                .createdTime(createdTime)
                .lastModifiedBy(lastmodifiedBy)
                .lastModifiedTime(lastmodifiedTime);

        SupplierBillReceipt supplierBillReceipt = new SupplierBillReceipt();
        return supplierBillReceipt.id(id)
                .tenantId(tenantId)
                .supplierBill(supplierBill)
                .materialReceipt(new MaterialReceipt().id(materialReceipt))
                .auditDetails(auditDetails);
    }
}
