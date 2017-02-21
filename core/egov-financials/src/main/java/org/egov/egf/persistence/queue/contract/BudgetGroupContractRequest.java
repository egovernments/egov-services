package  org.egov.egf.persistence.queue.contract;
import java.util.List;  
 import lombok.Data; 
public @Data class BudgetGroupContractRequest {
private RequestInfo requestInfo = null;
private List<BudgetGroupContract> budgetGroups;
private BudgetGroupContract budgetGroup;
private Page page=new Page();}