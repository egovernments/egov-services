package org.egov.egf.budget.web.contract;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoundaryResponse {

	@JsonProperty("Boundary")
	private List<Boundary> boundaries;

}
