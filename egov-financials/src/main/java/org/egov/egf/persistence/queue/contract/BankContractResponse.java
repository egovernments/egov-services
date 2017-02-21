package  org.egov.egf.persistence.queue.contract;
import java.util.ArrayList;
import java.util.List;  
 import lombok.Data; 
public @Data class BankContractResponse {
private ResponseInfo responseInfo = null;
private List<BankContract> banks=new ArrayList<BankContract>();
private BankContract bank;
private Page page=new Page();}