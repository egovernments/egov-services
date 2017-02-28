package org.egov.workflow.web.controller;

import java.util.List;

import org.egov.workflow.domain.service.Workflow;
import org.egov.workflow.persistence.entity.Task;
import org.egov.workflow.web.contract.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WorkFlowController {

    @Autowired
    private Workflow workflow;

    // @InitBinder("processInstance")
    // protected void initBinder(WebDataBinder binder) {
    // binder.addValidators(new ProcessInstanceValidator());
    // }

    @PostMapping(value = "/create")
    public ProcessInstance startWorkflow(@RequestBody final ProcessInstance processInstance) {
        String tenantId = processInstance.getRequestInfo().getTenantId();
        return workflow.start(tenantId, processInstance);
    }

    @PostMapping(value = "/close")
    public ProcessInstance endWorkflow(@RequestBody final ProcessInstance processInstance) {
        String tenantId = processInstance.getRequestInfo().getTenantId();
        return workflow.end(tenantId, processInstance);
    }

    @GetMapping(value = "/history")
    public List<Task> getHistory(@RequestParam final String tenantId , @RequestParam final String workflowId) {
        return workflow.getHistoryDetail(tenantId,workflowId);
    }

}
