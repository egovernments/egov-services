package org.egov.swm.persistence.entity;

import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.Boundary;
import org.egov.swm.domain.model.RefillingPumpStation;

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
public class RefillingPumpStationEntity {

	private String id = null;

	private String tenantId = null;

	private String ward = null;

	private String zone = null;

	private String street = null;

	private String colony = null;

	private String name = null;

	private String type = null;

	private String remarks = null;

	private String typeOfFuel = null;

	private Long quantity = null;

	private String createdBy = null;

	private String lastModifiedBy = null;

	private Long createdTime = null;

	private Long lastModifiedTime = null;

	public RefillingPumpStation toDomain() {

		RefillingPumpStation refillingPumpStation = new RefillingPumpStation();
		refillingPumpStation.setId(id);
		refillingPumpStation.setTenantId(tenantId);
		refillingPumpStation.setWard(Boundary.builder().code(ward).build());
		refillingPumpStation.setZone(Boundary.builder().code(zone).build());
		refillingPumpStation.setColony(Boundary.builder().code(colony).build());
		refillingPumpStation.setStreet(Boundary.builder().code(street).build());
		refillingPumpStation.setName(name);
		refillingPumpStation.setRemarks(remarks);
		refillingPumpStation.setType(type);
		refillingPumpStation.setTypeOfFuel(typeOfFuel);
		refillingPumpStation.setQuantity(quantity);
		refillingPumpStation.setAuditDetails(new AuditDetails());
		refillingPumpStation.getAuditDetails().setCreatedBy(createdBy);
		refillingPumpStation.getAuditDetails().setCreatedTime(createdTime);
		refillingPumpStation.getAuditDetails().setLastModifiedBy(lastModifiedBy);
		refillingPumpStation.getAuditDetails().setLastModifiedTime(lastModifiedTime);

		return refillingPumpStation;

	}

}
