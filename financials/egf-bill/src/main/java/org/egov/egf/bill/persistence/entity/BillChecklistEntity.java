package org.egov.egf.bill.persistence.entity;

import org.egov.egf.bill.domain.model.AuditDetails;
import org.egov.egf.bill.domain.model.BillChecklist;
import org.egov.egf.bill.domain.model.BillRegister;
import org.egov.egf.bill.domain.model.Checklist;

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
public class BillChecklistEntity {
    public static final String TABLE_NAME = "egf_billchecklist";
    public static final String SEQUENCE_NAME = "seq_egf_billchecklist";
    private String tenantId;
    private String id;
    private String bill;
    private String checklist;
    private String checklistValue;
    private String createdBy;
    private String lastModifiedBy;
    private Long createdTime;
    private Long lastModifiedTime;

    public BillChecklist toDomain() {
        final BillChecklist billChecklist = new BillChecklist();
        billChecklist.setTenantId(tenantId);
        billChecklist.setId(id);
        billChecklist.setBill(BillRegister.builder().billNumber(bill).build());
        billChecklist.setChecklist(Checklist.builder().code(checklist).build());
        billChecklist.setChecklistValue(checklistValue);
        billChecklist.setAuditDetails(new AuditDetails());
        billChecklist.getAuditDetails().setCreatedBy(createdBy);
        billChecklist.getAuditDetails().setCreatedTime(createdTime);
        billChecklist.getAuditDetails().setLastModifiedBy(lastModifiedBy);
        billChecklist.getAuditDetails().setLastModifiedTime(lastModifiedTime);
        return billChecklist;
    }

    public BillChecklistEntity toEntity(final BillChecklist billChecklist) {
        tenantId = billChecklist.getTenantId();
        id = billChecklist.getId();
        bill = billChecklist.getBill() != null ? billChecklist.getBill().getBillNumber() : null;
        checklist = billChecklist.getChecklist() != null ? billChecklist.getChecklist().getCode() : null;
        checklistValue = billChecklist.getChecklistValue();
        createdBy = billChecklist.getAuditDetails() != null ? billChecklist.getAuditDetails().getCreatedBy() : null;
        createdTime = billChecklist.getAuditDetails() != null ? billChecklist.getAuditDetails().getCreatedTime() : null;
        lastModifiedBy = billChecklist.getAuditDetails() != null ? billChecklist.getAuditDetails().getLastModifiedBy() : null;
        lastModifiedTime = billChecklist.getAuditDetails() != null ? billChecklist.getAuditDetails().getLastModifiedTime() : null;
        return this;
    }
}
