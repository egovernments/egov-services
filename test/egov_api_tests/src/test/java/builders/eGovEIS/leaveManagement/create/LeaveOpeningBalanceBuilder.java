package builders.eGovEIS.leaveManagement.create;

import entities.requests.eGovEIS.leaveManagement.create.LeaveOpeningBalance;
import entities.requests.eGovEIS.leaveManagement.create.LeaveType;

public final class LeaveOpeningBalanceBuilder {

    LeaveOpeningBalance leaveOpeningBalance = new LeaveOpeningBalance();
    LeaveType leaveType = new LeaveType();

    public LeaveOpeningBalanceBuilder() {
        leaveOpeningBalance.setLeaveType(leaveType);
        leaveOpeningBalance.setEmployee(1);
        leaveOpeningBalance.setCalendarYear(2017);
        leaveOpeningBalance.setNoOfDays(2017);
        leaveOpeningBalance.setTenantId("1");
    }

    public LeaveOpeningBalanceBuilder withLeaveType(LeaveType leaveType) {
        leaveOpeningBalance.setLeaveType(leaveType);
        return this;
    }

    public LeaveOpeningBalanceBuilder withCalendarYear(int calendarYear) {
        leaveOpeningBalance.setCalendarYear(calendarYear);
        return this;
    }

    public LeaveOpeningBalanceBuilder withTenantId(String tenantId) {
        leaveOpeningBalance.setTenantId(tenantId);
        return this;
    }

    public LeaveOpeningBalanceBuilder withId(String id) {
        leaveOpeningBalance.setId(id);
        return this;
    }

    public LeaveOpeningBalanceBuilder withEmployee(int employee) {
        leaveOpeningBalance.setEmployee(employee);
        return this;
    }

    public LeaveOpeningBalanceBuilder withNoOfDays(int noOfDays) {
        leaveOpeningBalance.setNoOfDays(noOfDays);
        return this;
    }

    public LeaveOpeningBalance build() {
        return leaveOpeningBalance;
    }
}
