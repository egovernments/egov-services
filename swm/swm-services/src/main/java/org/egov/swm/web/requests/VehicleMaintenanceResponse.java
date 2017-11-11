package org.egov.swm.web.requests;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.swm.domain.model.PaginationContract;
import org.egov.swm.domain.model.VehicleMaintenance;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@JsonInclude(value = Include.NON_NULL)
public @Data class VehicleMaintenanceResponse {

	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo;
	private List<VehicleMaintenance> vehicleMaintenances;
	private PaginationContract page;
}