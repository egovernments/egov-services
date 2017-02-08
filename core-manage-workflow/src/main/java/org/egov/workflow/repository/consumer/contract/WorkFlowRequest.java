package org.egov.workflow.repository.consumer.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class WorkFlowRequest {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo = null;

    @JsonProperty("ProcessInstance")
    private ProcessInstance processInstance = null;

    public ProcessInstance getProcessInstance() {
        return processInstance;
    }
    
}
