package org.egov.boundary.web.contract;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CityRequest {

	@Valid
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo = null;
	@Valid
	@JsonProperty("City")
	private City city = null;

}
