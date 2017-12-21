package org.egov.lcms.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 
 * @author Prasad 
 * This object holds information about the Advocate Charge
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdvocateCharge {

	@JsonProperty("charge")
	private String charge = null;

	@JsonProperty("caseDetails")
	private CaseDetails caseDetails = null;

	@JsonProperty("amount")
	private Double amount = null;

}
