package org.egov.egf.voucher.persistence.entity ;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class BudgetReAppropriationSearchEntity extends BudgetReAppropriationEntity { private Integer pageSize; 
private Integer offset; 
public BudgetReAppropriation toDomain(){ 
BudgetReAppropriation budgetReAppropriation = new BudgetReAppropriation (); 
super.toDomain( budgetReAppropriation);return budgetReAppropriation ;}
 
public BudgetReAppropriationSearchEntity toEntity( BudgetReAppropriationSearch budgetReAppropriationSearch){
super.toEntity(( BudgetReAppropriation)budgetReAppropriationSearch);
this.pageSize=budgetReAppropriationSearch.getPageSize(); this.offset=budgetReAppropriationSearch.getOffset(); return this;} 
 
} 