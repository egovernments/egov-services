package org.egov.demand.web.contract;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.common.contract.request.RequestInfo;
import org.egov.demand.model.Demand;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DemandRequest {

	@NotNull
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;
	
	@Valid
	@JsonProperty("Demands")
	private List<Demand> demands = new ArrayList<>();
}
