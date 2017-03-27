package org.egov.lams.contract;

import org.egov.lams.model.City;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CityResponse {
	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo = null;
	@JsonProperty("City")
	private City city = null;
}
