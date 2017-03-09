package  org.egov.egf.persistence.queue.contract;
import java.util.ArrayList;
import java.util.List;

import lombok.Data; 
public @Data class FundContractRequest {
private RequestInfo requestInfo = new RequestInfo();
private List<FundContract> funds =new ArrayList<FundContract>() ;
private FundContract fund =new FundContract() ;
private Pagination page=new Pagination();}