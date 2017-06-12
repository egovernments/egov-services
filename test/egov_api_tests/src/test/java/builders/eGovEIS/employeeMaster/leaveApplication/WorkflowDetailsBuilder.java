package builders.eGovEIS.employeeMaster.leaveApplication;

import entities.requests.eGovEIS.employeeMaster.leaveApplication.WorkflowDetails;

public class WorkflowDetailsBuilder {

    WorkflowDetails workflowDetails = new WorkflowDetails();

    public WorkflowDetailsBuilder(){
        workflowDetails.setDepartment("");
        workflowDetails.setDesignation("");
        workflowDetails.setAssignee(82);
        workflowDetails.setAction("");
        workflowDetails.setStatus("");
    }

    public WorkflowDetails build(){
        return workflowDetails;
    }
 }
