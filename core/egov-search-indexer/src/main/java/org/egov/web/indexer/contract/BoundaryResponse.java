package org.egov.web.indexer.contract;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BoundaryResponse {
	
	@JsonProperty("Boundary")
	private List<Boundary> boundaries;

}
