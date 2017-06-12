package entities.requests.pgrCollections.createComplaint;

public class Values
{
    private String receivingCenter;

    private String complainantAddress;

    private String status;

    private String receivingMode;

    private String locationId;

    private String stateId;

    private String departmentId;

    private String childLocationId;

    private String approvalComments;

    private String assignmentId;

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getChildLocationId() {
        return childLocationId;
    }

    public void setChildLocationId(String childLocationId) {
        this.childLocationId = childLocationId;
    }

    public String getApprovalComments() {
        return approvalComments;
    }

    public void setApprovalComments(String approvalComments) {
        this.approvalComments = approvalComments;
    }

    public String getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getReceivingCenter ()
    {
        return receivingCenter;
    }

    public void setReceivingCenter (String receivingCenter)
    {
        this.receivingCenter = receivingCenter;
    }

    public String getComplainantAddress ()
    {
        return complainantAddress;
    }

    public void setComplainantAddress (String complainantAddress)
    {
        this.complainantAddress = complainantAddress;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getReceivingMode ()
    {
        return receivingMode;
    }

    public void setReceivingMode (String receivingMode)
    {
        this.receivingMode = receivingMode;
    }
}
