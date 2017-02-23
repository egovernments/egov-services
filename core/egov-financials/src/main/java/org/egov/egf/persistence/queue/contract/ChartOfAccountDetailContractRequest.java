package  org.egov.egf.persistence.queue.contract;
import java.util.ArrayList;
import java.util.List;

import lombok.Data; 
public @Data class ChartOfAccountDetailContractRequest {
private RequestInfo requestInfo = new RequestInfo();
private List<ChartOfAccountDetailContract> chartOfAccountDetails =new ArrayList<ChartOfAccountDetailContract>() ;
private ChartOfAccountDetailContract chartOfAccountDetail =new ChartOfAccountDetailContract() ;
private Pagination page=new Pagination();}