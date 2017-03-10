package  org.egov.egf.persistence.queue.contract;
import java.util.ArrayList;
import java.util.List;

import lombok.Data; 
public @Data class AccountDetailKeyContractRequest {
private RequestInfo requestInfo = new RequestInfo();
private List<AccountDetailKeyContract> accountDetailKeys =new ArrayList<AccountDetailKeyContract>() ;
private AccountDetailKeyContract accountDetailKey =new AccountDetailKeyContract() ;
private Pagination page=new Pagination();}