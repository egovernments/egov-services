package org.egov.models;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author Anil
 *
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TaxExemptionRequest {

	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;
	
	@Valid
	@JsonProperty("taxExemption")
	TaxExemption taxExemption;
}
