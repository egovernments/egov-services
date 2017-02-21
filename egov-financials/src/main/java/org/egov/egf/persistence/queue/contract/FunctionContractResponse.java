package  org.egov.egf.persistence.queue.contract;
import java.util.List;  
 import lombok.Data; 
public @Data class FunctionContractResponse {
private ResponseInfo responseInfo = null;
private List<FunctionContract> functions;
private FunctionContract function;
private Page page=new Page();}