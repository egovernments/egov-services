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
public class CalculationFactorSearchCriteria {
	
	@NotNull
	@NotEmpty
	private String tenantId;
	
	@NotNull
	@NotEmpty
	private String factorType;
	
	@NotNull
	@NotEmpty
	private String validDate;
	
	private String code;
}
