package org.egov.demand.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxPeriod {

	private Long fromDate;

	private Long toDate;

	private String module;

	private String description;

	private String financialYear;
}
