package  org.egov.egf.persistence.queue.contract;
import java.util.List;  
 import lombok.Data; 
public @Data class FunctionaryContractResponse {
private ResponseInfo responseInfo = null;
private List<FunctionaryContract> functionaries;
private FunctionaryContract functionary;
private Page page=new Page();}