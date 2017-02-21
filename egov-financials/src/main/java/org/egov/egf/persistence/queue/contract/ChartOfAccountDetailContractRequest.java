package  org.egov.egf.persistence.queue.contract;
import java.util.List;  
 import lombok.Data; 
public @Data class ChartOfAccountDetailContractRequest {
private RequestInfo requestInfo = null;
private List<ChartOfAccountDetailContract> chartOfAccountDetails;
private ChartOfAccountDetailContract chartOfAccountDetail;
private Page page=new Page();}