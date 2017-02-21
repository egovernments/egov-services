package  org.egov.egf.persistence.queue.contract;
import java.util.List;  
 import lombok.Data; 
public @Data class AccountEntityContractResponse {
private ResponseInfo responseInfo = null;
private List<AccountEntityContract> accountEntities;
private AccountEntityContract accountEntity;
private Page page=new Page();}