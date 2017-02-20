package org.egov.workflow.web.controller;

import org.egov.workflow.service.Workflow;
import org.egov.workflow.web.contract.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WorkFlowController {

    @Autowired
    private Workflow workflow;

    @PostMapping(value = "/create")
    public ProcessInstance startWorkflow(@RequestBody final ProcessInstance processInstance) {
        String tenantId = processInstance.getRequestInfo().getTenantId();
        return workflow.start(tenantId, processInstance);
    }


}
