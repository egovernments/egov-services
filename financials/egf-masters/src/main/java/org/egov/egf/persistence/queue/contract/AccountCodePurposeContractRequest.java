package  org.egov.egf.persistence.queue.contract;
import java.util.ArrayList;
import java.util.List;

import lombok.Data; 
public @Data class AccountCodePurposeContractRequest {
private RequestInfo requestInfo = new RequestInfo();
private List<AccountCodePurposeContract> accountCodePurposes =new ArrayList<AccountCodePurposeContract>() ;
private AccountCodePurposeContract accountCodePurpose =new AccountCodePurposeContract() ;
private Pagination page=new Pagination();}