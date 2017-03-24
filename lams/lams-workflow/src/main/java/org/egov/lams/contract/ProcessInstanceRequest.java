package org.egov.lams.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import org.egov.lams.model.ProcessInstance;
import org.egov.lams.model.RequestInfo;

@Getter
@Setter
@ToString
public class ProcessInstanceRequest {
	
	@JsonProperty("requestInfo")
	private WorkflowRequestInfo requestInfo = null;

	@JsonProperty("processInstances")
	private List<ProcessInstance> processInstances = new ArrayList<ProcessInstance>();

	@JsonProperty("processInstance")
	private ProcessInstance processInstance = null;

}
