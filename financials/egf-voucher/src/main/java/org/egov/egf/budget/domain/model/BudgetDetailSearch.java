package org.egov.egf.budget.domain.model ;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class BudgetDetailSearch extends BudgetDetail{ private Integer pageSize; 
private Integer offset; 
} 