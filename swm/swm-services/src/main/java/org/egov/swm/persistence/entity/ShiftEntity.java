package org.egov.swm.persistence.entity;

import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.Shift;
import org.egov.swm.domain.model.ShiftType;
import org.egov.swm.web.contract.Department;
import org.egov.swm.web.contract.Designation;

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
public class ShiftEntity {

    private String code = null;

    private String tenantId = null;

    private String shiftType = null;

    private String department = null;

    private String designation = null;

    private Long shiftStartTime = null;

    private Long shiftEndTime = null;

    private Long lunchStartTime = null;

    private Long lunchEndTime = null;

    private Long graceTimeFrom = null;

    private Long graceTimeTo = null;

    private String remarks = null;

    private String createdBy = null;

    private String lastModifiedBy = null;

    private Long createdTime = null;

    private Long lastModifiedTime = null;

    public Shift toDomain() {

        final Shift shift = new Shift();
        shift.setCode(code);
        shift.setTenantId(tenantId);
        shift.setShiftType(ShiftType.builder().code(shiftType).build());
        shift.setDepartment(Department.builder().code(department).build());
        shift.setDesignation(Designation.builder().code(designation).build());
        shift.setShiftStartTime(shiftStartTime);
        shift.setShiftEndTime(shiftEndTime);
        shift.setLunchStartTime(lunchStartTime);
        shift.setLunchEndTime(lunchEndTime);
        shift.setGraceTimeFrom(graceTimeFrom);
        shift.setGraceTimeTo(graceTimeTo);
        shift.setRemarks(remarks);
        shift.setAuditDetails(new AuditDetails());
        shift.getAuditDetails().setCreatedBy(createdBy);
        shift.getAuditDetails().setCreatedTime(createdTime);
        shift.getAuditDetails().setLastModifiedBy(lastModifiedBy);
        shift.getAuditDetails().setLastModifiedTime(lastModifiedTime);

        return shift;

    }

}
