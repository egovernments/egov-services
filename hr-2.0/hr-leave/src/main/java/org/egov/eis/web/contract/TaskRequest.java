package org.egov.eis.web.contract;

import lombok.Data;
import org.egov.common.contract.request.RequestInfo;

import java.util.ArrayList;
import java.util.List;

public @Data class TaskRequest {
    private RequestInfo requestInfo = new RequestInfo();
    private List<Task> tasks = new ArrayList<Task>();
    private Task task = new Task();
    private Pagination page = new Pagination();
}