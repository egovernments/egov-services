package  org.egov.egf.persistence.queue.contract;
import java.util.List;  
 import lombok.Data; 
public @Data class SubSchemeContractRequest {
private RequestInfo requestInfo = null;
private List<SubSchemeContract> subSchemes;
private SubSchemeContract subScheme;
private Page page=new Page();}