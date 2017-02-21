package  org.egov.egf.persistence.queue.contract;
import java.util.List;  
 import lombok.Data; 
public @Data class ChartOfAccountDetailContractResponse {
private ResponseInfo responseInfo = null;
private List<ChartOfAccountDetailContract> chartOfAccountDetails;
private ChartOfAccountDetailContract chartOfAccountDetail;
private Page page=new Page();}