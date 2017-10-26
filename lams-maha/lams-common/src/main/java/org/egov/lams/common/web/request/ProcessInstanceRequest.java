package org.egov.lams.common.web.request;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.lams.common.web.contract.ProcessInstance;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProcessInstanceRequest {
	
	@JsonProperty("requestInfo")
	private RequestInfo requestInfo = null;

	@JsonProperty("processInstances")
	private List<ProcessInstance> processInstances = new ArrayList<ProcessInstance>();

	@JsonProperty("processInstance")
	private ProcessInstance processInstance = null;

}
