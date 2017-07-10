package org.egov.egf.budget.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BudgetSearch extends Budget {
	private Integer pageSize;
	private Integer offSet;
}