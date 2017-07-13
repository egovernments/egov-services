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

public class BudgetDetailSearchEntity extends BudgetDetailEntity { private Integer pageSize; 
private Integer offset; 
public BudgetDetail toDomain(){ 
BudgetDetail budgetDetail = new BudgetDetail (); 
super.toDomain( budgetDetail);return budgetDetail ;}
 
public BudgetDetailSearchEntity toEntity( BudgetDetailSearch budgetDetailSearch){
super.toEntity(( BudgetDetail)budgetDetailSearch);
this.pageSize=budgetDetailSearch.getPageSize(); this.offset=budgetDetailSearch.getOffset(); return this;} 
 
} 