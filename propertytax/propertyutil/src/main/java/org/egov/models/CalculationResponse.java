package org.egov.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalculationResponse {
	
	private ResponseInfo responseInfo;
	
	private List<TaxCalculation> taxes;

}
