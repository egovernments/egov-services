package org.egov.egf.budget.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class BudgetReAppropriationSearch extends BudgetReAppropriation {
	private Integer pageSize;
	private Integer offset;
	private String sortBy;
}