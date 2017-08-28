package org.egov.tl.workflow.model;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;

import lombok.Data;

public @Data class TaskRequest {

	private RequestInfo requestInfo = new RequestInfo();

	private List<Task> tasks = new ArrayList<Task>();

	private Task task = new Task();

	private Pagination page = new Pagination();
}