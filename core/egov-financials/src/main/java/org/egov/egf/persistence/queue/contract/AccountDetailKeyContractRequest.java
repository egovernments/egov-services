package  org.egov.egf.persistence.queue.contract;
import java.util.List;  
 import lombok.Data; 
public @Data class AccountDetailKeyContractRequest {
private RequestInfo requestInfo = null;
private List<AccountDetailKeyContract> accountDetailKeys;
private AccountDetailKeyContract accountDetailKey;
private Page page=new Page();}