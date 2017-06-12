package entities.requests.eGovEIS.employeeMaster.leaveApplication;

public class WorkflowDetails {
    private String action;
    private String designation;
    private int assignee;
    private String department;
    private String status;

    public String getAction() {
        return this.action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDesignation() {
        return this.designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public int getAssignee() {
        return this.assignee;
    }

    public void setAssignee(int assignee) {
        this.assignee = assignee;
    }

    public String getDepartment() {
        return this.department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
