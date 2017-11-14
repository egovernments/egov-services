package org.egov.lcms.notification.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
