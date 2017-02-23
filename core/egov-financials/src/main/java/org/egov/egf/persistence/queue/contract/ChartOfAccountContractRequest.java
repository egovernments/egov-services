package  org.egov.egf.persistence.queue.contract;
import java.util.ArrayList;
import java.util.List;

import lombok.Data; 
public @Data class ChartOfAccountContractRequest {
private RequestInfo requestInfo = new RequestInfo();
private List<ChartOfAccountContract> chartOfAccounts =new ArrayList<ChartOfAccountContract>() ;
private ChartOfAccountContract chartOfAccount =new ChartOfAccountContract() ;
private Pagination page=new Pagination();}