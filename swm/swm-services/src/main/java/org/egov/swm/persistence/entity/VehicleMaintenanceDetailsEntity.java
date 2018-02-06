package org.egov.swm.persistence.entity;

import org.egov.swm.domain.enums.MaintenanceType;
import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.Vehicle;
import org.egov.swm.domain.model.VehicleMaintenanceDetails;

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
public class VehicleMaintenanceDetailsEntity {

    private String transactionNo;

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

    public VehicleMaintenanceDetails toDomain() {
        return VehicleMaintenanceDetails.builder().transactionNo(transactionNo).tenantId(tenantid).isScheduled(isscheduled)
                .vehicle(Vehicle.builder().regNumber(vehicle).build()).actualMaintenanceDate(actualmaintenancedate)
                .vehicleScheduledMaintenanceDate(vehiclescheduledmaintenancedate)
                .vehicleDowntimeActual(vehicledowntimeactual).vehicleDownTimeActualUom(vehicledowntimeactualuom)
                .vehicleReadingDuringMaintenance(vehiclereadingduringmaintenance).remarks(remarks)
                .costIncurred(costincurred)
                .maintenanceType(MaintenanceType.valueOf(maintenancetype))
                .auditDetails(AuditDetails.builder().createdBy(createdBy).createdTime(createdTime)
                        .lastModifiedBy(lastModifiedBy).lastModifiedTime(lastModifiedTime).build())
                .build();
    }
}
