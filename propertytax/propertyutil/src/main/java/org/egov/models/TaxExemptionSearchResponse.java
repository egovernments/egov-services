package org.egov.models;

import java.util.List;

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
public class TaxExemptionSearchResponse {

	@JsonProperty("responseInfo")
	private ResponseInfo responseInfo;

	@JsonProperty("taxExemption")
	private List<TaxExemption> taxExemptions;

}
