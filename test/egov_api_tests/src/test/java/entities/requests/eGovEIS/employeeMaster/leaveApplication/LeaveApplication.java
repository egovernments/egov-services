package entities.requests.eGovEIS.employeeMaster.leaveApplication;

public class LeaveApplication {
    private String fromDate;
    private String reason;
    private int leaveDays;
    private LeaveType leaveType;
    private WorkflowDetails workflowDetails;
    private String toDate;
    private String stateId;
    private String tenantId;
    private int employee;
    private int availableDays;
    private String status;

    public String getFromDate() {
        return this.fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getLeaveDays() {
        return this.leaveDays;
    }

    public void setLeaveDays(int leaveDays) {
        this.leaveDays = leaveDays;
    }

    public LeaveType getLeaveType() {
        return this.leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public WorkflowDetails getWorkflowDetails() {
        return this.workflowDetails;
    }

    public void setWorkflowDetails(WorkflowDetails workflowDetails) {
        this.workflowDetails = workflowDetails;
    }

    public String getToDate() {
        return this.toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getStateId() {
        return this.stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public int getEmployee() {
        return this.employee;
    }

    public void setEmployee(int employee) {
        this.employee = employee;
    }

    public int getAvailableDays() {
        return this.availableDays;
    }

    public void setAvailableDays(int availableDays) {
        this.availableDays = availableDays;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
