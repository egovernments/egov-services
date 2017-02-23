package  org.egov.egf.persistence.queue.contract;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;@JsonInclude(value=Include.NON_NULL)public @Data class FunctionaryContractResponse {
private ResponseInfo responseInfo ;
private List<FunctionaryContract> functionaries;
private FunctionaryContract functionary ;
private Pagination page;}