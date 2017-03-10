package  org.egov.egf.persistence.queue.contract;
import java.util.ArrayList;
import java.util.List;

import lombok.Data; 
public @Data class BudgetGroupContractRequest {
private RequestInfo requestInfo = new RequestInfo();
private List<BudgetGroupContract> budgetGroups =new ArrayList<BudgetGroupContract>() ;
private BudgetGroupContract budgetGroup =new BudgetGroupContract() ;
private Pagination page=new Pagination();}