package org.egov.swm.persistence.entity;

import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.DumpingGround;
import org.egov.swm.domain.model.Route;
import org.egov.swm.domain.model.SanitationStaffTarget;
import org.egov.swm.domain.model.SwmProcess;
import org.egov.swm.web.contract.Employee;

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
public class SanitationStaffTargetEntity {

    private String tenantId = null;

    private String targetNo = null;

    private Long targetFrom = null;

    private Long targetTo = null;

    private String swmProcess = null;

    private String route = null;

    private String employee = null;

    private Double targetedGarbage = null;

    private Double wetWaste = null;

    private Double dryWaste = null;

    private String dumpingGround = null;

    private String createdBy = null;

    private String lastModifiedBy = null;

    private Long createdTime = null;

    private Long lastModifiedTime = null;

    public SanitationStaffTarget toDomain() {

        final SanitationStaffTarget sanitationStaffTarget = new SanitationStaffTarget();
        sanitationStaffTarget.setTenantId(tenantId);
        sanitationStaffTarget.setTargetNo(targetNo);
        sanitationStaffTarget.setTargetFrom(targetFrom);
        sanitationStaffTarget.setTargetTo(targetTo);
        sanitationStaffTarget.setSwmProcess(SwmProcess.builder().code(swmProcess).build());
        sanitationStaffTarget.setRoute(Route.builder().code(route).build());
        sanitationStaffTarget.setEmployee(Employee.builder().code(employee).build());
        sanitationStaffTarget.setTargetedGarbage(targetedGarbage);
        sanitationStaffTarget.setDryWaste(dryWaste);
        sanitationStaffTarget.setWetWaste(wetWaste);
        sanitationStaffTarget.setDumpingGround(DumpingGround.builder().code(dumpingGround).build());
        sanitationStaffTarget.setAuditDetails(new AuditDetails());
        sanitationStaffTarget.getAuditDetails().setCreatedBy(createdBy);
        sanitationStaffTarget.getAuditDetails().setCreatedTime(createdTime);
        sanitationStaffTarget.getAuditDetails().setLastModifiedBy(lastModifiedBy);
        sanitationStaffTarget.getAuditDetails().setLastModifiedTime(lastModifiedTime);

        return sanitationStaffTarget;

    }

}
