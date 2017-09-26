package org.egov.voucher.workflow.service;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.web.contract.ProcessInstance;
import org.egov.common.web.contract.ProcessInstanceRequest;
import org.egov.common.web.contract.ProcessInstanceResponse;
import org.egov.common.web.contract.TaskContract;
import org.egov.common.web.contract.TaskRequest;
import org.egov.common.web.contract.TaskResponse;
import org.egov.egf.voucher.web.contract.VoucherContract;
import org.egov.voucher.workflow.repository.WorkflowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkflowService {

    private WorkflowRepository workflowRepository;

    @Autowired
    public WorkflowService(WorkflowRepository workflowRepository) {
        this.workflowRepository = workflowRepository;
    }

    public void enrichWorkflow(VoucherContract voucher, RequestInfo requestInfo) {

        if (isWorkflowCreate(voucher.getState())) {

            ProcessInstanceResponse processInstanceResponse = new ProcessInstanceResponse();
            ProcessInstanceRequest request = getProcessInstanceRequest(voucher.getState(),
                    voucher.getTenantId());

            request.setRequestInfo(requestInfo);

            processInstanceResponse = workflowRepository.start(request);

            if (processInstanceResponse != null)
                update(processInstanceResponse, voucher);

        } else if (isWorkflowUpdate(voucher.getState())) {

            TaskResponse taskResponse = new TaskResponse();
            TaskRequest taskRequest = getTaskRequest(voucher.getState(), voucher.getTenantId());
            taskRequest.setRequestInfo(requestInfo);

            taskResponse = workflowRepository.update(taskRequest);

            if (taskResponse != null)
                update(taskResponse, voucher);

        }

    }

    private ProcessInstanceRequest getProcessInstanceRequest(TaskContract task, String tenantId) {

        ProcessInstanceRequest request = new ProcessInstanceRequest();
        ProcessInstance processInstance = new ProcessInstance();

        if (task != null) {
            processInstance.setBusinessKey(task.getBusinessKey());
            processInstance.setType(task.getType());
            processInstance.setComments(task.getComments());
            processInstance.setTenantId(tenantId);
            processInstance.setAssignee(task.getAssignee());
            processInstance.setInitiatorPosition(null != task.getAssignee() ? task.getAssignee().getId() : null);
            processInstance.setSenderName(task.getSenderName());
            processInstance.setDetails(task.getDetails());
            processInstance.setStatus(task.getStatus());
        }
        request.setProcessInstance(processInstance);

        return request;
    }

    private TaskRequest getTaskRequest(TaskContract task, String tenantId) {

        TaskRequest request = new TaskRequest();
        task.setTenantId(tenantId);
        request.setTask(task);

        return request;
    }

    private boolean isWorkflowCreate(TaskContract task) {
        return task != null && task.getAction() != null
                && task.getAction().equalsIgnoreCase("create");
    }

    private boolean isWorkflowUpdate(TaskContract task) {
        return task != null && task.getAction() != null && !task.getAction().isEmpty();
    }

    private void update(ProcessInstanceResponse processInstanceResponse, VoucherContract voucher) {
        if (voucher != null) {
            voucher.getState().setId(processInstanceResponse.getProcessInstance().getId());
        }
    }

    private void update(TaskResponse taskResponse, VoucherContract voucher) {

        if (null != voucher && null != voucher.getState()) {
            voucher.getState().setId(taskResponse.getTask().getId());
        }
    }

}
