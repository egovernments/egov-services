package  org.egov.egf.persistence.queue.contract;
import java.util.List;  
 import lombok.Data; 
public @Data class SupplierContractRequest {
private RequestInfo requestInfo = null;
private List<SupplierContract> suppliers;
private SupplierContract supplier;
private Page page=new Page();}