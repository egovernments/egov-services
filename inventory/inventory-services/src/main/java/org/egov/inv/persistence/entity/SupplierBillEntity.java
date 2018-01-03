package org.egov.inv.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.egov.inv.model.AuditDetails;
import org.egov.inv.model.Store;
import org.egov.inv.model.SupplierBill;
import org.egov.inv.model.User;

import static org.springframework.util.StringUtils.isEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierBillEntity {

    private String id;

    private String tenantId;

    private String store;

    private String invoiceNumber;

    private Long invoiceDate;

    private Long approvedDate;

    private Long approvedBy;

    private String cancellationReason;

    private String cancellationRemarks;

    private String stateId;

    private String status;

    private String billId;

    private String createdBy;

    private Long createdTime;

    private String lastmodifiedBy;

    private Long lastmodifiedTime;

    public SupplierBill toDomain() {
        AuditDetails auditDetails = new AuditDetails();
        auditDetails.createdBy(createdBy)
                .createdTime(createdTime)
                .lastModifiedBy(lastmodifiedBy)
                .lastModifiedTime(lastmodifiedTime);

        SupplierBill supplierBill = new SupplierBill();
        return supplierBill.id(id)
                .tenantId(tenantId)
                .store(!isEmpty(store) ? new Store().code(store) : null)
                .invoiceNumber(invoiceNumber)
                .invoiceDate(invoiceDate)
                .approvedDate(approvedDate)
                .approvedBy(null != approvedBy ? new User().id(approvedBy) : null)
                .cancellationReason(cancellationReason)
                .cancellationRemarks(cancellationRemarks)
                .stateId(stateId)
                .supplierBillStatus(status)
                .billId(billId)
                .auditDetails(auditDetails);
    }
}
