package org.egov.egf.voucher.persistence.entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Builder
public class BudgetDetailEntity extends AuditableEntity
{
public static final String TABLE_NAME ="egf_budgetdetail";
private String id;
private String budgetId;
private String budgetGroupId;
private String usingDepartmentId;
private String executingDepartmentId;
private String fundId;
private String functionId;
private String schemeId;
private String subSchemeId;
private String functionaryId;
private String boundaryId;
private BigDecimal anticipatoryAmount;
private BigDecimal originalAmount;
private BigDecimal approvedAmount;
private BigDecimal planningPercent;
private BigDecimal budgetAvailable;
private String statusId;
private String documentNumber;
private String uniqueNo;
private String materializedPath;
public BudgetDetail toDomain(){ 
BudgetDetail budgetDetail = new BudgetDetail (); 
super.toDomain( budgetDetail);budgetDetail.setId(this.id);
budgetDetail.setBudget(Budget.builder().id(budgetId).build());
budgetDetail.setBudgetGroup(BudgetGroupContract.builder().id(budgetGroupId).build());
budgetDetail.setUsingDepartment(DepartmentContract.builder().id(usingDepartmentId).build());
budgetDetail.setExecutingDepartment(DepartmentContract.builder().id(executingDepartmentId).build());
budgetDetail.setFund(FundContract.builder().id(fundId).build());
budgetDetail.setFunction(FunctionContract.builder().id(functionId).build());
budgetDetail.setScheme(SchemeContract.builder().id(schemeId).build());
budgetDetail.setSubScheme(SubSchemeContract.builder().id(subSchemeId).build());
budgetDetail.setFunctionary(FunctionContract.builder().id(functionaryId).build());
budgetDetail.setBoundary(BoundaryContract.builder().id(boundaryId).build());
budgetDetail.setAnticipatoryAmount(this.anticipatoryAmount);
budgetDetail.setOriginalAmount(this.originalAmount);
budgetDetail.setApprovedAmount(this.approvedAmount);
budgetDetail.setPlanningPercent(this.planningPercent);
budgetDetail.setBudgetAvailable(this.budgetAvailable);
budgetDetail.setStatus(EgfStatus.builder().id(statusId).build());
budgetDetail.setDocumentNumber(this.documentNumber);
budgetDetail.setUniqueNo(this.uniqueNo);
budgetDetail.setMaterializedPath(this.materializedPath);
return budgetDetail ;}

public BudgetDetailEntity toEntity( BudgetDetail budgetDetail ){
super.toEntity(( Auditable)budgetDetail);
this.id=budgetDetail.getId();
this.budgetId=budgetDetail.getBudget()!=null?budgetDetail.getBudget().getId():null;
this.budgetGroupId=budgetDetail.getBudgetGroup()!=null?budgetDetail.getBudgetGroup().getId():null;
this.usingDepartmentId=budgetDetail.getUsingDepartment()!=null?budgetDetail.getUsingDepartment().getId():null;
this.executingDepartmentId=budgetDetail.getExecutingDepartment()!=null?budgetDetail.getExecutingDepartment().getId():null;
this.fundId=budgetDetail.getFund()!=null?budgetDetail.getFund().getId():null;
this.functionId=budgetDetail.getFunction()!=null?budgetDetail.getFunction().getId():null;
this.schemeId=budgetDetail.getScheme()!=null?budgetDetail.getScheme().getId():null;
this.subSchemeId=budgetDetail.getSubScheme()!=null?budgetDetail.getSubScheme().getId():null;
this.functionaryId=budgetDetail.getFunctionary()!=null?budgetDetail.getFunctionary().getId():null;
this.boundaryId=budgetDetail.getBoundary()!=null?budgetDetail.getBoundary().getId():null;
this.anticipatoryAmount=budgetDetail.getAnticipatoryAmount();
this.originalAmount=budgetDetail.getOriginalAmount();
this.approvedAmount=budgetDetail.getApprovedAmount();
this.planningPercent=budgetDetail.getPlanningPercent();
this.budgetAvailable=budgetDetail.getBudgetAvailable();
this.statusId=budgetDetail.getStatus()!=null?budgetDetail.getStatus().getId():null;
this.documentNumber=budgetDetail.getDocumentNumber();
this.uniqueNo=budgetDetail.getUniqueNo();
this.materializedPath=budgetDetail.getMaterializedPath();
return this;} 

}
