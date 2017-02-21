package  org.egov.egf.persistence.queue.contract;
import java.util.List;  
 import lombok.Data; 
public @Data class ChartOfAccountContractRequest {
private RequestInfo requestInfo = null;
private List<ChartOfAccountContract> chartOfAccounts;
private ChartOfAccountContract chartOfAccount;
private Page page=new Page();}