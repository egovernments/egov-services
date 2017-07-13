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

public class BudgetSearchEntity extends BudgetEntity { private Integer pageSize; 
private Integer offset; 
public Budget toDomain(){ 
Budget budget = new Budget (); 
super.toDomain( budget);return budget ;}
 
public BudgetSearchEntity toEntity( BudgetSearch budgetSearch){
super.toEntity(( Budget)budgetSearch);
this.pageSize=budgetSearch.getPageSize(); this.offset=budgetSearch.getOffset(); return this;} 
 
} 