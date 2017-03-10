package  org.egov.egf.persistence.queue.contract;
import java.util.ArrayList;
import java.util.List;

import lombok.Data; 
public @Data class BankAccountContractRequest {
private RequestInfo requestInfo = new RequestInfo();
private List<BankAccountContract> bankAccounts =new ArrayList<BankAccountContract>() ;
private BankAccountContract bankAccount =new BankAccountContract() ;
private Pagination page=new Pagination();}