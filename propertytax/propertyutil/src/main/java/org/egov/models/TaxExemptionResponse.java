package org.egov.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TaxExemptionResponse {

	@JsonProperty("responseInfo")
	private ResponseInfo responseInfo;

	@JsonProperty("taxExemption")
	private TaxExemption taxExemption;
}

