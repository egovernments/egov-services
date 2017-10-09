package org.egov.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TaxRates {

	private Long id;

	@NotNull
	@Size(min = 4, max = 128)
	private String tenantId;

	@NotNull
	private String taxHead;

	private String dependentTaxHead;

	@NotNull
	private String fromDate;

	private String toDate;

	@NotNull
	private Double fromValue;

	@NotNull
	private Double toValue;

	private Double ratePercentage;

	private Double taxFlatValue;

	private String propertyType;

	private String usage;

	private AuditDetails auditDetails;

}
