package org.egov.boundary.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CityResponse {
	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo = null;
	@JsonProperty("City")
	private City city = null;
}
