package  org.egov.egf.persistence.queue.contract;
import java.util.List;  
 import lombok.Data; 
public @Data class AccountCodePurposeContractRequest {
private RequestInfo requestInfo = null;
private List<AccountCodePurposeContract> accountCodePurposes;
private AccountCodePurposeContract accountCodePurpose;
private Page page=new Page();}