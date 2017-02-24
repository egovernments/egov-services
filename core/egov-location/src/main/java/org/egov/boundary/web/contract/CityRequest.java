package org.egov.boundary.web.contract;

import javax.validation.Valid;

import org.egov.boundary.persistence.entity.City;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CityRequest {

	@Valid
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo = null;
	@Valid
	@JsonProperty("City")
	private City city = null;

}
