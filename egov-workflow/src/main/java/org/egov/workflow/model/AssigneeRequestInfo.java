package org.egov.workflow.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AssigneeRequestInfo {

    @JsonProperty("ResposneInfo")
    private RequestInfo requestInfo = null;

    @JsonProperty("AssigneeFilterInfo")
    private AssigneeFilterInfo assigneeFilterInfo = null;

    public AssigneeRequestInfo() {

    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(final RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public AssigneeFilterInfo getAssigneeFilterInfo() {
        return assigneeFilterInfo;
    }

    public void setAssigneeFilterInfo(final AssigneeFilterInfo assigneeFilterInfo) {
        this.assigneeFilterInfo = assigneeFilterInfo;
    }

}
