package  org.egov.egf.persistence.queue.contract;
import java.util.ArrayList;
import java.util.List;

import lombok.Data; 
public @Data class SupplierContractRequest {
private RequestInfo requestInfo = new RequestInfo();
private List<SupplierContract> suppliers =new ArrayList<SupplierContract>() ;
private SupplierContract supplier =new SupplierContract() ;
private Pagination page=new Pagination();}