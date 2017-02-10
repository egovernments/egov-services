package org.egov.workflow.web.controller;

import org.egov.workflow.service.WorkflowInterface;
import org.egov.workflow.web.contract.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WorkFlowController {

    @Autowired
    private WorkflowInterface workflowInterface;

    //    @InitBinder("processInstance")
//    protected void initBinder(WebDataBinder binder) {
//        binder.addValidators(new ProcessInstanceValidator());
//    }


    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProcessInstance startWorkflow(@RequestBody final ProcessInstance processInstance) {
        String tenantId = processInstance.getRequestInfo().getTenantId();
        return workflowInterface.start(tenantId, processInstance);
    }


}
