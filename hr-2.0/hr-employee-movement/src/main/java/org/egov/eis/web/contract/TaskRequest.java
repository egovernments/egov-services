package org.egov.eis.web.contract;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

public @Data class TaskRequest {
    private RequestInfo requestInfo = new RequestInfo();
    private List<Task> tasks = new ArrayList<Task>();
    private Task task = new Task();
    private Pagination page = new Pagination();
}