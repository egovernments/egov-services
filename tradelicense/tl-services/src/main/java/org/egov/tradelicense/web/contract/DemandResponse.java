package org.egov.tradelicense.web.contract;

import java.util.ArrayList;
import java.util.List;

import org.egov.tl.commons.web.contract.ResponseInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DemandResponse {

	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo;

	@JsonProperty("Demands")
	private List<Demand> demands = new ArrayList<Demand>();
}
