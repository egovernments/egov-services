package  org.egov.egf.persistence.queue.contract;
import java.util.List;  
 import lombok.Data; 
public @Data class FundsourceContractRequest {
private RequestInfo requestInfo = null;
private List<FundsourceContract> fundsources;
private FundsourceContract fundsource;
private Page page=new Page();}