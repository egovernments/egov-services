package entities.requests.eGovEIS.leaveManagement.create;

public class LeaveOpeningBalance {
    private LeaveType leaveType;
    private int calendarYear;
    private String tenantId;
    private String id;
    private int employee;
    private int noOfDays;

    public LeaveType getLeaveType() {
        return this.leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public int getCalendarYear() {
        return this.calendarYear;
    }

    public void setCalendarYear(int calendarYear) {
        this.calendarYear = calendarYear;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getEmployee() {
        return this.employee;
    }

    public void setEmployee(int employee) {
        this.employee = employee;
    }

    public int getNoOfDays() {
        return this.noOfDays;
    }

    public void setNoOfDays(int noOfDays) {
        this.noOfDays = noOfDays;
    }
}
