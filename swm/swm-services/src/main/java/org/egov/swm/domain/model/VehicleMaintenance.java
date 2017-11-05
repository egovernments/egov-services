package org.egov.swm.domain.model;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class VehicleMaintenance {

	@Size(min = 1, max = 256)
	@JsonProperty("code")
	private String code = null;

	@NotNull

	@Size(min = 1, max = 128)
	@JsonProperty("tenantId")
	private String tenantId = null;

	@NotNull

	@JsonProperty("vehicle")
	private Vehicle vehicle = null;

	@Min(1)
	@Max(500)
	@JsonProperty("maintenanceAfterDays")
	private Long maintenanceAfterDays = null;

	@Min(1)
	@Max(20000)
	@JsonProperty("maintenanceAfterKm")
	private Long maintenanceAfterKm = null;

	@NotNull
	@JsonProperty("downtimeforMaintenance")
	private Double downtimeforMaintenance = null;

	@Valid
	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;
}
