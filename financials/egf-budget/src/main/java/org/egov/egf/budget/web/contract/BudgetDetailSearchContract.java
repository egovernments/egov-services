package org.egov.egf.budget.web.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BudgetDetailSearchContract extends BudgetDetailContract {
	private Integer pageSize;
	private Integer offSet;
}