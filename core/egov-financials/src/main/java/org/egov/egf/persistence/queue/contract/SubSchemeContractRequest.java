package  org.egov.egf.persistence.queue.contract;
import java.util.ArrayList;
import java.util.List;

import lombok.Data; 
public @Data class SubSchemeContractRequest {
private RequestInfo requestInfo = new RequestInfo();
private List<SubSchemeContract> subSchemes =new ArrayList<SubSchemeContract>() ;
private SubSchemeContract subScheme =new SubSchemeContract() ;
private Pagination page=new Pagination();}