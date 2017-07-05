package org.egov.egf.budget.persistence.entity ;
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.BudgetSearch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class BudgetSearchEntity extends BudgetEntity { private Integer pageSize; 
private Integer offset; 
@Override
public Budget toDomain(){ 
Budget budget = new Budget (); 
super.toDomain( budget);return budget ;}
 
public BudgetSearchEntity toEntity( BudgetSearch budgetSearch){
super.toEntity(budgetSearch);
this.pageSize=budgetSearch.getPageSize(); this.offset=budgetSearch.getOffset(); return this;} 
 
} 