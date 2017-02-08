package org.egov.workflow.domain.service;

import org.egov.workflow.domain.model.WorkflowProcess;
import org.egov.workflow.repository.producer.WorkflowProducer;
import org.egov.workflow.repository.producer.contract.WorkflowRequest;
import org.egov.workflow.web.contract.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkflowService {

    private WorkflowProducer workflowProducer;

    @Autowired
    public WorkflowService(WorkflowProducer workflowProducer) {
        this.workflowProducer = workflowProducer;
    }

    public void publish(ProcessInstance processInstance) {
        WorkflowProcess workflowProcess = processInstance.asDomain();
        workflowProducer.handle(new WorkflowRequest().fromDomain(workflowProcess));
    }


}
