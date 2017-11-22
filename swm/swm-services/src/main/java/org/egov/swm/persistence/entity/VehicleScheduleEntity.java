package org.egov.swm.persistence.entity;

import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.Route;
import org.egov.swm.domain.model.Vehicle;
import org.egov.swm.domain.model.VehicleSchedule;

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
public class VehicleScheduleEntity {

    private String transactionNo = null;

    private String tenantId = null;

    private Long scheduledFrom = null;

    private Long scheduledTo = null;

    private String route = null;

    private String vehicle = null;

    private Double targetedGarbage = null;

    private String createdBy = null;

    private String lastModifiedBy = null;

    private Long createdTime = null;

    private Long lastModifiedTime = null;

    public VehicleSchedule toDomain() {

        final VehicleSchedule vehicleSchedule = new VehicleSchedule();
        vehicleSchedule.setTransactionNo(transactionNo);
        vehicleSchedule.setTenantId(tenantId);
        vehicleSchedule.setScheduledFrom(scheduledFrom);
        vehicleSchedule.setScheduledTo(scheduledTo);
        vehicleSchedule.setRoute(Route.builder().code(route).build());
        vehicleSchedule.setVehicle(Vehicle.builder().regNumber(vehicle).build());
        vehicleSchedule.setTargetedGarbage(targetedGarbage);
        vehicleSchedule.setAuditDetails(new AuditDetails());
        vehicleSchedule.getAuditDetails().setCreatedBy(createdBy);
        vehicleSchedule.getAuditDetails().setCreatedTime(createdTime);
        vehicleSchedule.getAuditDetails().setLastModifiedBy(lastModifiedBy);
        vehicleSchedule.getAuditDetails().setLastModifiedTime(lastModifiedTime);

        return vehicleSchedule;

    }

}
