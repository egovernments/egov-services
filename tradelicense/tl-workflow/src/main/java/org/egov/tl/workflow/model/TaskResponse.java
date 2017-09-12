package org.egov.tl.workflow.model;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@JsonInclude(value = Include.NON_NULL)
public @Data class TaskResponse {

	private ResponseInfo responseInfo;

	private List<Task> tasks;

	private Task task;

	private Pagination page;
}