package org.egov.works.workflow.contracts;

import java.util.ArrayList;
import java.util.List;

import org.egov.works.estimate.web.contract.RequestInfo;
import lombok.Data;

public @Data class ProcessInstanceRequest {
	private RequestInfo requestInfo = new RequestInfo();

	private List<ProcessInstance> processInstances = new ArrayList<ProcessInstance>();

	private ProcessInstance processInstance = new ProcessInstance();

	private Pagination page = new Pagination();
}