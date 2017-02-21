package  org.egov.egf.persistence.queue.contract;
import java.util.List;  
 import lombok.Data; 
public @Data class AccountDetailTypeContractRequest {
private RequestInfo requestInfo = null;
private List<AccountDetailTypeContract> accountDetailTypes;
private AccountDetailTypeContract accountDetailType;
private Page page=new Page();}