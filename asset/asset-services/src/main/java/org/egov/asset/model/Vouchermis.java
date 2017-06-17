package org.egov.asset.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
public class Vouchermis {

	private Long id;

	private FundSource fundsource;

	private Integer billNumber;

	private Long boundary;

	private Long departmentId;

	private Scheme scheme;

	private SubScheme subScheme;

	private Functionary functionary;

	private Function function;

	private String sourcePath;

	private String budgetAppropriationNo;

	private Boolean budgetCheckRequired;

}
