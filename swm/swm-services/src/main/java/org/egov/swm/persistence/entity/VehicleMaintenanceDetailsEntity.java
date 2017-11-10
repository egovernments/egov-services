package org.egov.swm.persistence.entity;

import lombok.*;
import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.Vehicle;
import org.egov.swm.domain.model.VehicleMaintenanceDetails;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleMaintenanceDetailsEntity {

    private String code;

    private String tenantid;

    private Boolean isscheduled;

    private String maintenancetype;

    private String vehicle;

    private Long actualmaintenancedate;

    private Long vehiclescheduledmaintenancedate;

    private Double vehicledowntimeactual;

    private String vehicledowntimeactualuom;

    private Double vehiclereadingduringmaintenance;

    private String remarks;

    private Double costincurred;

    private String createdBy;

    private String lastModifiedBy;

    private Long createdTime;

    private Long lastModifiedTime;

    public VehicleMaintenanceDetails toDomain(){
        return VehicleMaintenanceDetails.builder()
                   .code(code)
                   .tenantId(tenantid)
                   .isScheduled(isscheduled)
                   .vehicle(Vehicle.builder().regNumber(vehicle).build())
                   .actualMaintenanceDate(actualmaintenancedate)
                   .vehicleScheduledMaintenanceDate(vehiclescheduledmaintenancedate)
                   .vehicleDowntimeActual(vehicledowntimeactual)
                   .vehicleDownTimeActualUom(vehicledowntimeactualuom)
                   .vehicleReadingDuringMaintenance(vehiclereadingduringmaintenance)
                   .remarks(remarks)
                   .costIncurred(costincurred)
                   .auditDetails(AuditDetails.builder().createdBy(createdBy)
                           .createdTime(createdTime).lastModifiedBy(lastModifiedBy)
                           .lastModifiedTime(lastModifiedTime)
                           .build())
                   .build();
    }
}
