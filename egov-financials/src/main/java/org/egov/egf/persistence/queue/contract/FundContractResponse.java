package  org.egov.egf.persistence.queue.contract;
import java.util.List;  
 import lombok.Data; 
public @Data class FundContractResponse {
private ResponseInfo responseInfo = null;
private List<FundContract> funds;
private FundContract fund;
private Page page=new Page();}