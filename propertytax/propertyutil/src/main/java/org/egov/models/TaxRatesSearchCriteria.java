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
public class TaxRatesSearchCriteria {
	
	@NotNull
	@NotEmpty
	private String tenantId;
	
	private String taxHead;
	
	@NotNull
	@NotEmpty
	private String validDate;
	
	private Double validARVAmount;
	
	private String parentTaxHead;
	
	private String usage;
	
	private String propertyType;
}
