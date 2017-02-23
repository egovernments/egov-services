package  org.egov.egf.persistence.queue.contract;
import java.util.ArrayList;
import java.util.List;

import lombok.Data; 
public @Data class AccountDetailTypeContractRequest {
private RequestInfo requestInfo = new RequestInfo();
private List<AccountDetailTypeContract> accountDetailTypes =new ArrayList<AccountDetailTypeContract>() ;
private AccountDetailTypeContract accountDetailType =new AccountDetailTypeContract() ;
private Pagination page=new Pagination();}