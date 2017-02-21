package  org.egov.egf.persistence.queue.contract;
import java.util.List;  
 import lombok.Data; 
public @Data class BankBranchContractRequest {
private RequestInfo requestInfo = null;
private List<BankBranchContract> bankBranches;
private BankBranchContract bankBranch;
private Page page=new Page();}