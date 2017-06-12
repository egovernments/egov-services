package entities.responses.eGovEIS.employeeMaster.LeaveApplication;

public class LeaveApplication {
    private String reason;
    private Object halfdays;
    private String applicationNumber;
    private Object lastModifiedDate;
    private WorkflowDetails workflowDetails;
    private String toDate;
    private Object stateId;
    private Object lastModifiedBy;
    private Object firstHalfleave;
    private int employee;
    private String fromDate;
    private double leaveDays;
    private Object createdDate;
    private LeaveType leaveType;
    private Object createdBy;
    private Object compensatoryForDate;
    private String tenantId;
    private Object id;
    private double availableDays;
    private String status;

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Object getHalfdays() {
        return this.halfdays;
    }

    public void setHalfdays(Object halfdays) {
        this.halfdays = halfdays;
    }

    public String getApplicationNumber() {
        return this.applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public Object getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public void setLastModifiedDate(Object lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
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

    public Object getStateId() {
        return this.stateId;
    }

    public void setStateId(Object stateId) {
        this.stateId = stateId;
    }

    public Object getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public void setLastModifiedBy(Object lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Object getFirstHalfleave() {
        return this.firstHalfleave;
    }

    public void setFirstHalfleave(Object firstHalfleave) {
        this.firstHalfleave = firstHalfleave;
    }

    public int getEmployee() {
        return this.employee;
    }

    public void setEmployee(int employee) {
        this.employee = employee;
    }

    public String getFromDate() {
        return this.fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public double getLeaveDays() {
        return this.leaveDays;
    }

    public void setLeaveDays(double leaveDays) {
        this.leaveDays = leaveDays;
    }

    public Object getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(Object createdDate) {
        this.createdDate = createdDate;
    }

    public LeaveType getLeaveType() {
        return this.leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public Object getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(Object createdBy) {
        this.createdBy = createdBy;
    }

    public Object getCompensatoryForDate() {
        return this.compensatoryForDate;
    }

    public void setCompensatoryForDate(Object compensatoryForDate) {
        this.compensatoryForDate = compensatoryForDate;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Object getId() {
        return this.id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public double getAvailableDays() {
        return this.availableDays;
    }

    public void setAvailableDays(double availableDays) {
        this.availableDays = availableDays;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
