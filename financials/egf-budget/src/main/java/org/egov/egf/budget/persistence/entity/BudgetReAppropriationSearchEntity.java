package org.egov.egf.budget.persistence.entity ;
import org.egov.egf.budget.domain.model.BudgetReAppropriation;
import org.egov.egf.budget.domain.model.BudgetReAppropriationSearch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class BudgetReAppropriationSearchEntity extends BudgetReAppropriationEntity { private Integer pageSize; 
private Integer offset; 
@Override
public BudgetReAppropriation toDomain(){ 
BudgetReAppropriation budgetReAppropriation = new BudgetReAppropriation (); 
super.toDomain( budgetReAppropriation);return budgetReAppropriation ;}
 
public BudgetReAppropriationSearchEntity toEntity( BudgetReAppropriationSearch budgetReAppropriationSearch){
super.toEntity(budgetReAppropriationSearch);
this.pageSize=budgetReAppropriationSearch.getPageSize(); this.offset=budgetReAppropriationSearch.getOffset(); return this;} 
 
} 