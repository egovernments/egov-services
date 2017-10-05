package org.egov.calculator.models;

import java.util.List;

import org.egov.models.TaxPeriod;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TaxperiodWrapper {

	private List<UnitWrapper> units;

	private TaxPeriod taxPeriod;

}
