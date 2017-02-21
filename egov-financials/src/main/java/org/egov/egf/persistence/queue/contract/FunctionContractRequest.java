package  org.egov.egf.persistence.queue.contract;
import java.util.List;  
 import lombok.Data; 
public @Data class FunctionContractRequest {
private RequestInfo requestInfo = null;
private List<FunctionContract> functions;
private FunctionContract function;
private Page page=new Page();}