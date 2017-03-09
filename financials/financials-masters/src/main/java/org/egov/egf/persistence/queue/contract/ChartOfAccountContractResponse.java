package  org.egov.egf.persistence.queue.contract;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;@JsonInclude(value=Include.NON_NULL)public @Data class ChartOfAccountContractResponse {
private ResponseInfo responseInfo ;
private List<ChartOfAccountContract> chartOfAccounts;
private ChartOfAccountContract chartOfAccount ;
private Pagination page;}