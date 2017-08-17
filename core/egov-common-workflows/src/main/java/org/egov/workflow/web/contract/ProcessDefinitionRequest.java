package org.egov.workflow.web.contract;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

public @Data class ProcessDefinitionRequest {
	private RequestInfo requestInfo = new RequestInfo();
	
	private List<ProcessDefinition> processDefinitions = new ArrayList<ProcessDefinition>();
	
	private ProcessDefinition processDefinition = new ProcessDefinition();

}
