package org.egov.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxRatesRequest {

	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;
	
	private List<TaxRates> taxRates;
}
