package  org.egov.egf.persistence.queue.contract;
import java.util.List;  
 import lombok.Data; 
public @Data class SchemeContractResponse {
private ResponseInfo responseInfo = null;
private List<SchemeContract> schemes;
private SchemeContract scheme;
private Page page=new Page();}