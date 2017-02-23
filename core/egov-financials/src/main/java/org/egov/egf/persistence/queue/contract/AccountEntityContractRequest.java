package  org.egov.egf.persistence.queue.contract;
import java.util.ArrayList;
import java.util.List;

import lombok.Data; 
public @Data class AccountEntityContractRequest {
private RequestInfo requestInfo = new RequestInfo();
private List<AccountEntityContract> accountEntities =new ArrayList<AccountEntityContract>() ;
private AccountEntityContract accountEntity =new AccountEntityContract() ;
private Pagination page=new Pagination();}