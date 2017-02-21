package  org.egov.egf.persistence.queue.contract;
import java.util.List;  
 import lombok.Data; 
public @Data class FinancialYearContractResponse {
private ResponseInfo responseInfo = null;
private List<FinancialYearContract> financialYears;
private FinancialYearContract financialYear;
private Page page=new Page();}