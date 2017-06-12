package builders.eGovEIS.employeeMaster.leaveApplication;

import entities.requests.eGovEIS.employeeMaster.leaveApplication.LeaveApplication;
import entities.requests.eGovEIS.employeeMaster.leaveApplication.LeaveType;
import entities.requests.eGovEIS.employeeMaster.leaveApplication.WorkflowDetails;

public class LeaveApplicationBuilder {

    LeaveApplication leaveApplication = new LeaveApplication();
    LeaveType leaveType = new LeaveTypeBuilder().build();
    WorkflowDetails workflowDetails = new WorkflowDetailsBuilder().build();

    public LeaveApplicationBuilder(){
        leaveApplication.setEmployee(82);
        leaveApplication.setFromDate("05/05/2017");
        leaveApplication.setToDate("05/05/2017");
        leaveApplication.setAvailableDays(6);
        leaveApplication.setLeaveDays(2);
        leaveApplication.setReason("Not feeling well");
        leaveApplication.setStatus("");
        leaveApplication.setStateId("");
        leaveApplication.setTenantId("ap.kurnool");
        leaveApplication.setLeaveType(leaveType);
        leaveApplication.setWorkflowDetails(workflowDetails);
    }

    public LeaveApplication build(){
        return leaveApplication;
    }
}
