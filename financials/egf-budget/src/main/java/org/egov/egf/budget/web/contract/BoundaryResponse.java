package org.egov.egf.budget.web.contract;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class BoundaryResponse {

	@JsonProperty("Boundary")
	private List<Boundary> boundaries;

}
