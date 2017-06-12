package builders.eGovEIS.leaveManagement.create;

import entities.requests.eGovEIS.leaveManagement.create.LeaveType;

public class LeaveTypeBuilder {

    LeaveType leaveType = new LeaveType();

    public LeaveTypeBuilder() {
        leaveType.setId(2);
    }

    public LeaveTypeBuilder withId(int id) {
        leaveType.setId(id);
        return this;
    }

    public LeaveType build() {
        return leaveType;
    }
}
