package  org.egov.egf.persistence.queue.contract;
import java.util.List;  
 import lombok.Data; 
public @Data class BankContractRequest {
private RequestInfo requestInfo = null;
private List<BankContract> banks;
private BankContract bank;
private Page page=new Page();}