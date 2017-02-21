package  org.egov.egf.persistence.queue.contract;
import java.util.List;  
 import lombok.Data; 
public @Data class FiscalPeriodContractRequest {
private RequestInfo requestInfo = null;
private List<FiscalPeriodContract> fiscalPeriods;
private FiscalPeriodContract fiscalPeriod;
private Page page=new Page();}