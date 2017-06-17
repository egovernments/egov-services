package org.egov.models;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalculationRequest {

	@JsonProperty("RequestInfo")
	private	 RequestInfo requestInfo;

	private Property property;	

	private Integer	taxEffectiveDate;	

	@NotNull
	private String	calculationType;	

	@NotNull
	private String	taxPeriod;	

}
