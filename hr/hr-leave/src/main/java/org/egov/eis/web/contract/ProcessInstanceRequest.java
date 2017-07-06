package org.egov.eis.web.contract;

import lombok.Data;
import org.egov.common.contract.request.RequestInfo;

import java.util.ArrayList;
import java.util.List;

public @Data class ProcessInstanceRequest {
    private RequestInfo requestInfo = new RequestInfo();

    private List<ProcessInstance> processInstances = new ArrayList<ProcessInstance>();

    private ProcessInstance processInstance = new ProcessInstance();

    private Pagination page = new Pagination();
}