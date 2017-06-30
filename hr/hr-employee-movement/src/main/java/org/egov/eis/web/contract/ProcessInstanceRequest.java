package org.egov.eis.web.contract;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

public @Data class ProcessInstanceRequest {
    private RequestInfo requestInfo = new RequestInfo();

    private List<ProcessInstance> processInstances = new ArrayList<ProcessInstance>();

    private ProcessInstance processInstance = new ProcessInstance();

    private Pagination page = new Pagination();
}