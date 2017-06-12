package builders.eGovEIS.employeeMaster.leaveApplication;

import entities.requests.eGovEIS.employeeMaster.leaveApplication.LeaveType;

public class LeaveTypeBuilder {

    LeaveType leaveType = new LeaveType();

    public LeaveTypeBuilder(){
        leaveType.setId("120");
    }

    public LeaveType build(){
        return leaveType;
    }
}
