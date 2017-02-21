package  org.egov.egf.persistence.queue.contract;
import java.util.List;  
 import lombok.Data; 
public @Data class BankAccountContractRequest {
private RequestInfo requestInfo = null;
private List<BankAccountContract> bankAccounts;
private BankAccountContract bankAccount;
private Page page=new Page();}