package org.egov.models;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommonTaxDetails {

	@NotNull
	private String occupancyDate;

	@NotNull
	private String effectiveDate;

	private Double depreciation;

	private Double rebateValue;

	@NotNull
	private Double calculatedARV;

	private Double manualRV;

	private Double residentialRV;

	private Double nonResidentialRV;

	@NotNull
	private Double totalTax;

	private List<HeadWiseTax> headWiseTaxes;
	
	private Double calculatedRV;
	
	private Double calculatedMRV;
	

}
