package org.egov.calculator.models;

import java.util.List;
import java.util.Map;

import org.egov.models.TaxRates;
import org.egov.models.Unit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UnitWrapper {

	private Unit unit;

	private String floorNo;

	private Map<String, Double> factors;

	private List<TaxRates> taxRates;

	private Double guidanceValue;

	private Double factorsValue;

}
