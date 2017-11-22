package org.egov.swm.persistence.entity;

import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.SanitationStaffSchedule;
import org.egov.swm.domain.model.SanitationStaffTarget;
import org.egov.swm.domain.model.Shift;

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
public class SanitationStaffScheduleEntity {

    private String tenantId = null;

    private String transactionNo = null;

    private String sanitationStaffTarget = null;

    private String shift = null;

    private String createdBy = null;

    private String lastModifiedBy = null;

    private Long createdTime = null;

    private Long lastModifiedTime = null;

    public SanitationStaffSchedule toDomain() {

        final SanitationStaffSchedule sanitationStaffSchedule = new SanitationStaffSchedule();
        sanitationStaffSchedule.setTenantId(tenantId);
        sanitationStaffSchedule.setTransactionNo(transactionNo);
        sanitationStaffSchedule
                .setSanitationStaffTarget(SanitationStaffTarget.builder().targetNo(sanitationStaffTarget).build());
        sanitationStaffSchedule.setShift(Shift.builder().code(shift).build());
        sanitationStaffSchedule.setAuditDetails(new AuditDetails());
        sanitationStaffSchedule.getAuditDetails().setCreatedBy(createdBy);
        sanitationStaffSchedule.getAuditDetails().setCreatedTime(createdTime);
        sanitationStaffSchedule.getAuditDetails().setLastModifiedBy(lastModifiedBy);
        sanitationStaffSchedule.getAuditDetails().setLastModifiedTime(lastModifiedTime);

        return sanitationStaffSchedule;

    }

}
