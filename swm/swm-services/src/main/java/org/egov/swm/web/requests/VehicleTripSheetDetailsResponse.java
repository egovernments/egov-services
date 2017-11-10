package org.egov.swm.web.requests;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.VehicleTripSheetDetails;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@JsonInclude(value = Include.NON_NULL)
public @Data class VehicleTripSheetDetailsResponse {

	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo;
	private List<VehicleTripSheetDetails> vehicleTripSheetDetails;
	private Pagination page;
}