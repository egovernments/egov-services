package org.egov.workflow.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.egov.workflow.web.contract.AssigneeFilterInfo;
import org.egov.workflow.web.contract.RequestInfo;

@Getter
public class AssigneeRequestInfo {

    @JsonProperty("ResposneInfo")
    private RequestInfo requestInfo = null;

    @JsonProperty("AssigneeFilterInfo")
    private AssigneeFilterInfo assigneeFilterInfo = null;

}
