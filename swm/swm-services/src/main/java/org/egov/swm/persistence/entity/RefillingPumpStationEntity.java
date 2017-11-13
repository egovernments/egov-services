package org.egov.swm.persistence.entity;

import org.egov.swm.domain.model.*;

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

	private String code = null;

	private String tenantId = null;

	private String location = null;

	private String name = null;

	private String typeOfPump = null;

	private String remarks = null;

	private String typeOfFuel = null;

	private Double quantity = null;

	private String createdBy;

	private String lastModifiedBy;

	private Long createdTime;

	private Long lastModifiedTime;

	public RefillingPumpStation toDomain() {

		RefillingPumpStation refillingPumpStation = new RefillingPumpStation();
		refillingPumpStation.setCode(code);
		refillingPumpStation.setTenantId(tenantId);
		refillingPumpStation.setLocation(Boundary.builder().code(location).build());
		refillingPumpStation.setName(name);
		refillingPumpStation.setRemarks(remarks);
		refillingPumpStation.setTypeOfPump(OilCompanyName.builder().code(typeOfPump).build());
		refillingPumpStation.setTypeOfFuel(FuelType.builder().code(typeOfFuel).build());
		refillingPumpStation.setQuantity(quantity);
		refillingPumpStation.setAuditDetails(AuditDetails.builder().createdBy(createdBy)
				.createdTime(createdTime).lastModifiedBy(lastModifiedBy)
				.lastModifiedTime(lastModifiedTime)
				.build());

		return refillingPumpStation;

	}

}
