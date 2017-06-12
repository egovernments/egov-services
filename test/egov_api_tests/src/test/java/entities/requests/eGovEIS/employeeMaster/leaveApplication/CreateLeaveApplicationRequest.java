package entities.requests.eGovEIS.employeeMaster.leaveApplication;

import org.codehaus.jackson.annotate.JsonProperty;

public class CreateLeaveApplicationRequest {

    @JsonProperty("LeaveApplication")
    private LeaveApplication[] LeaveApplication;

    @JsonProperty("RequestInfo")
    private RequestInfo RequestInfo;

    public LeaveApplication[] getLeaveApplication() {
        return this.LeaveApplication;
    }

    public void setLeaveApplication(LeaveApplication[] LeaveApplication) {
        this.LeaveApplication = LeaveApplication;
    }

    public RequestInfo getRequestInfo() {
        return this.RequestInfo;
    }

    public void setRequestInfo(RequestInfo RequestInfo) {
        this.RequestInfo = RequestInfo;
    }
}
