package  org.egov.egf.persistence.queue.contract;
import java.util.List;  
 import lombok.Data; 
public @Data class SubSchemeContractResponse {
private ResponseInfo responseInfo = null;
private List<SubSchemeContract> subSchemes;
private SubSchemeContract subScheme;
private Page page=new Page();}