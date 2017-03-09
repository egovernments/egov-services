package  org.egov.egf.persistence.queue.contract;
import java.util.ArrayList;
import java.util.List;

import lombok.Data; 
public @Data class FinancialYearContractRequest {
private RequestInfo requestInfo = new RequestInfo();
private List<FinancialYearContract> financialYears =new ArrayList<FinancialYearContract>() ;
private FinancialYearContract financialYear =new FinancialYearContract() ;
private Pagination page=new Pagination();}