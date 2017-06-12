package entities.responses.eGovEIS.leaveManagement.search;

import org.codehaus.jackson.annotate.JsonProperty;

import java.math.BigInteger;

public class LeaveOpeningBalance {
    private String createdDate;
    @JsonProperty("LeaveType")
    private LeaveType leaveType;
    private BigInteger createdBy;
    private String lastModifiedDate;
    private int calendarYear;
    private BigInteger lastModifiedBy;
    private String tenantId;
    private int id;
    private int employee;
    private int noOfDays;

    public String getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public LeaveType getLeaveType() {
        return this.leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public BigInteger getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(BigInteger createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public int getCalendarYear() {
        return this.calendarYear;
    }

    public void setCalendarYear(int calendarYear) {
        this.calendarYear = calendarYear;
    }

    public BigInteger getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public void setLastModifiedBy(BigInteger lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
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
