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
public class TaxCalculation {

	@NotNull
	private String fromDate;

	@NotNull
	private String toDate;

	@NotNull
	private String effectiveDate;

	@NotNull
	private CommonTaxDetails propertyTaxes;

	@NotNull
	private List<UnitTax> unitTaxes;
}
