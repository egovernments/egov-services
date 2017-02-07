package org.egov.workflow.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.egov.workflow.contract.RequestInfo;

@Getter
public class AssigneeRequestInfo {

    @JsonProperty("ResposneInfo")
    private RequestInfo requestInfo = null;

    @JsonProperty("AssigneeFilterInfo")
    private AssigneeFilterInfo assigneeFilterInfo = null;

}
