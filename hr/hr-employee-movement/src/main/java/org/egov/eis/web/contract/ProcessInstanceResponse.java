package org.egov.eis.web.contract;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@JsonInclude(value = Include.NON_NULL)
public @Data class ProcessInstanceResponse {
    private ResponseInfo responseInfo;
    private List<ProcessInstance> processInstances;
    private ProcessInstance processInstance;
    private Pagination page;
}