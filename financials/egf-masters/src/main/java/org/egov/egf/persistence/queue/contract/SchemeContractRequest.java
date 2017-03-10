package  org.egov.egf.persistence.queue.contract;
import java.util.ArrayList;
import java.util.List;

import lombok.Data; 
public @Data class SchemeContractRequest {
private RequestInfo requestInfo = new RequestInfo();
private List<SchemeContract> schemes =new ArrayList<SchemeContract>() ;
private SchemeContract scheme =new SchemeContract() ;
private Pagination page=new Pagination();}