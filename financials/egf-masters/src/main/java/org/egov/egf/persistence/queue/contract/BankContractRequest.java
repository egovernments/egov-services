package  org.egov.egf.persistence.queue.contract;
import java.util.ArrayList;
import java.util.List;

import lombok.Data; 
public @Data class BankContractRequest {

private RequestInfo requestInfo = new RequestInfo();
private List<BankContract> banks=new ArrayList<BankContract>();
private BankContract bank=new BankContract();
private Pagination page=new Pagination();}