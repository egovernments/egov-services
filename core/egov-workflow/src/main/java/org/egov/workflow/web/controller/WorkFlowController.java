package org.egov.workflow.web.controller;

import org.egov.workflow.domain.service.Workflow;
import org.egov.workflow.persistence.entity.Task;
import org.egov.workflow.web.contract.ProcessInstance;
import org.egov.workflow.web.contract.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WorkFlowController {

    private Workflow workflow;

    @Autowired
    public WorkFlowController(Workflow workflow) {
        this.workflow = workflow;
    }

    @PostMapping(value = "/v1/_create")
    public ProcessInstance startWorkflow(@RequestBody final ProcessInstance processInstance) {
        String tenantId = processInstance.getTenantId();
        return workflow.start(tenantId, processInstance);
    }

    @PostMapping(value = "/v1/_close")
    public ProcessInstance endWorkflow(@RequestBody final ProcessInstance processInstance) {
        String tenantId = processInstance.getTenantId();
        return workflow.end(tenantId, processInstance);
    }

    @PostMapping(value = "/history/v1/_search")
    public List<Task> getHistory(@RequestParam(value = "tenantId", required = true) final String tenantId, @RequestParam final String workflowId, @RequestBody RequestInfoWrapper requestInfoWrapper) {
        return workflow.getHistoryDetail(tenantId, workflowId);
    }

    @PostMapping(value = "/v1/_update")
    public Task createTask(@RequestBody final Task task) {
        String tenantId = task.getTenantId();
        return workflow.update(tenantId, task);
    }

}
