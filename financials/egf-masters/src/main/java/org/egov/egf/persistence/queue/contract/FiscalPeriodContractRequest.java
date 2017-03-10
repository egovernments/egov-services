package  org.egov.egf.persistence.queue.contract;
import java.util.ArrayList;
import java.util.List;

import lombok.Data; 
public @Data class FiscalPeriodContractRequest {
private RequestInfo requestInfo = new RequestInfo();
private List<FiscalPeriodContract> fiscalPeriods =new ArrayList<FiscalPeriodContract>() ;
private FiscalPeriodContract fiscalPeriod =new FiscalPeriodContract() ;
private Pagination page=new Pagination();}