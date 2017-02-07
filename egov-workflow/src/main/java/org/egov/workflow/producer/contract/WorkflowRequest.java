package org.egov.workflow.producer.contract;

import lombok.Data;
import org.egov.workflow.contract.ProcessInstance;

import java.util.Date;

@Data
public class WorkflowRequest {

    private String tenantId;
    private String status;
    private String type;
    private Long assignee;
    private Date createdDate;

    public WorkflowRequest fromProcessInstance(ProcessInstance processInstance) {
        this.tenantId = processInstance.getRequestInfo().getTenantId();
        this.status = processInstance.getStatus();
        this.type = processInstance.getType();
        this.assignee = processInstance.getAssignee();
        this.createdDate = processInstance.getCreatedDate();
        return this;
    }
}
