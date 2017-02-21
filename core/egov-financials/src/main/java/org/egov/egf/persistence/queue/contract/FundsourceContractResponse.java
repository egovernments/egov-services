package  org.egov.egf.persistence.queue.contract;
import java.util.List;  
 import lombok.Data; 
public @Data class FundsourceContractResponse {
private ResponseInfo responseInfo = null;
private List<FundsourceContract> fundsources;
private FundsourceContract fundsource;
private Page page=new Page();}