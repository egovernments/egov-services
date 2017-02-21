package  org.egov.egf.persistence.queue.contract;
import java.util.List;  
 import lombok.Data; 
public @Data class FundContractRequest {
private RequestInfo requestInfo = null;
private List<FundContract> funds;
private FundContract fund;
private Page page=new Page();}