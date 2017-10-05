package org.egov.calculator.models;

import java.util.List;

import org.egov.models.CalculationResponse;
import org.egov.models.Property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TaxCalculationWrapper {

	private Property property;

	private List<TaxperiodWrapper> taxPeriods;

	private CalculationResponse calculationResponse;
}
