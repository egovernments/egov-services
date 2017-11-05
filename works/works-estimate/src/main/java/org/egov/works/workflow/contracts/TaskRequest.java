package org.egov.works.workflow.contracts;

import java.util.ArrayList;
import java.util.List;

import org.egov.works.estimate.web.contract.RequestInfo;
import lombok.Data;

public @Data class TaskRequest {

	private RequestInfo requestInfo = new RequestInfo();

	private List<Task> tasks = new ArrayList<Task>();

	private Task task = new Task();

	private Pagination page = new Pagination();
}