package org.egov.workflow.web.contract;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@JsonInclude(value = Include.NON_NULL)
public @Data class ProcessDefinitionResponse {

	private ResponseInfo responseInfo;

	private List<ProcessDefinition> processDefinitions = new ArrayList<ProcessDefinition>();

	private ProcessDefinition processDefinition = new ProcessDefinition();

}
