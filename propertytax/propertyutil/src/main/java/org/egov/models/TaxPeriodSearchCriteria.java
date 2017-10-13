package org.egov.models;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaxPeriodSearchCriteria {

	@NotNull
	@NotEmpty
	private String tenantId;

	private String validDate;

	private String code;

	private String fromDate;

	private String toDate;

	private String sortTaxPeriod;
}