package org.egov.swm.persistence.entity;

import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.Vehicle;
import org.egov.swm.domain.model.VehicleMaintenance;

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
public class VehicleMaintenanceEntity {

	private String tenantId = null;

	private String code = null;

	private String vehicle = null;

	private Long maintenanceAfterDays = null;

	private Long maintenanceAfterKm = null;

	private Double downtimeforMaintenance = null;

	private String createdBy = null;

	private String lastModifiedBy = null;

	private Long createdTime = null;

	private Long lastModifiedTime = null;

	public VehicleMaintenance toDomain() {

		VehicleMaintenance vehicleMaintenance = new VehicleMaintenance();
		vehicleMaintenance.setCode(code);
		vehicleMaintenance.setTenantId(tenantId);
		vehicleMaintenance.setVehicle(Vehicle.builder().regNumber(vehicle).build());
		vehicleMaintenance.setDowntimeforMaintenance(downtimeforMaintenance);
		vehicleMaintenance.setMaintenanceAfterDays(maintenanceAfterDays);
		vehicleMaintenance.setMaintenanceAfterKm(maintenanceAfterKm);
		vehicleMaintenance.setAuditDetails(new AuditDetails());
		vehicleMaintenance.getAuditDetails().setCreatedBy(createdBy);
		vehicleMaintenance.getAuditDetails().setCreatedTime(createdTime);
		vehicleMaintenance.getAuditDetails().setLastModifiedBy(lastModifiedBy);
		vehicleMaintenance.getAuditDetails().setLastModifiedTime(lastModifiedTime);

		return vehicleMaintenance;

	}

}
