package  org.egov.egf.persistence.queue.contract;
import java.util.ArrayList;
import java.util.List;

import lombok.Data; 
public @Data class FunctionaryContractRequest {
private RequestInfo requestInfo = new RequestInfo();
private List<FunctionaryContract> functionaries =new ArrayList<FunctionaryContract>() ;
private FunctionaryContract functionary =new FunctionaryContract() ;
private Pagination page=new Pagination();}