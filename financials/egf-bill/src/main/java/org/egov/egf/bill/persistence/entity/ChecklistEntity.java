package org.egov.egf.bill.persistence.entity;

import org.egov.egf.bill.domain.model.AuditDetails;
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
public class ChecklistEntity {
    public static final String TABLE_NAME = "egf_billchecklist";
    public static final String SEQUENCE_NAME = "seq_egf_billchecklist";
    private String tenantId;
    private String code;
    private String type;
    private String subType;
    private String key;
    private String description;
    private String createdBy;
    private String lastModifiedBy;
    private Long createdTime;
    private Long lastModifiedTime;

    public Checklist toDomain() {
        final Checklist checklist = new Checklist();
        checklist.setTenantId(tenantId);
        checklist.setCode(code);
        checklist.setType(type);
        checklist.setSubType(subType);
        checklist.setKey(key);
        checklist.setDescription(description);
        checklist.setAuditDetails(new AuditDetails());
        checklist.getAuditDetails().setCreatedBy(createdBy);
        checklist.getAuditDetails().setCreatedTime(createdTime);
        checklist.getAuditDetails().setLastModifiedBy(lastModifiedBy);
        checklist.getAuditDetails().setLastModifiedTime(lastModifiedTime);
        return checklist;
    }

    public ChecklistEntity toEntity(final Checklist checklist) {
        tenantId = checklist.getTenantId();
        code = checklist.getCode();
        type = checklist.getType();
        subType = checklist.getSubType();
        key = checklist.getKey();
        description = checklist.getDescription();
        createdBy = checklist.getAuditDetails() != null ? checklist.getAuditDetails().getCreatedBy() : null;
        createdTime = checklist.getAuditDetails() != null ? checklist.getAuditDetails().getCreatedTime() : null;
        lastModifiedBy = checklist.getAuditDetails() != null ? checklist.getAuditDetails().getLastModifiedBy() : null;
        lastModifiedTime = checklist.getAuditDetails() != null ? checklist.getAuditDetails().getLastModifiedTime() : null;
        return this;
    }
}
