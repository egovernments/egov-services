package  org.egov.egf.persistence.queue.contract;
import java.util.ArrayList;
import java.util.List;

import lombok.Data; 
public @Data class FundsourceContractRequest {
private RequestInfo requestInfo = new RequestInfo();
private List<FundsourceContract> fundsources =new ArrayList<FundsourceContract>() ;
private FundsourceContract fundsource =new FundsourceContract() ;
private Pagination page=new Pagination();}