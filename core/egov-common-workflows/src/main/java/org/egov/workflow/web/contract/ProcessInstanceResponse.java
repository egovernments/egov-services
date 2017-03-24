package org.egov.workflow.web.contract;

import java.util.List;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public @Data class ProcessInstanceResponse {
	private ResponseInfo responseInfo;
	private List<ProcessInstance> processInstances;
	private ProcessInstance processInstance;
	private Pagination page;
}