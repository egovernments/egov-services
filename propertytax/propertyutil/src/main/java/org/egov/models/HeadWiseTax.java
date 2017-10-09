package org.egov.models;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HeadWiseTax {

	@NotNull
	private String taxName;

	private Integer taxDays;

	@NotNull
	private Double taxValue;
}
