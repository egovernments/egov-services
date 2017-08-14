package org.egov.pgr.employee.enrichment.repository.contract;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import org.egov.common.contract.response.ResponseInfo;

import java.util.List;

@JsonInclude(value = Include.NON_NULL)
public @Data class ProcessInstanceResponse {
    private ResponseInfo responseInfo;
    private List<ProcessInstance> processInstances;
    private ProcessInstance processInstance;
    private Pagination page;
}