package org.egov.asset.contract;

import javax.validation.Valid;

import org.egov.asset.model.Boundary;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BoundaryRequest {

	@Valid
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo = null;
	@Valid
	@JsonProperty("Boundary")
	private Boundary  boundary  = null;
	 
}
