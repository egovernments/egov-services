package entities.requests.eGovEIS.employeeMaster.leaveType.create;

import entities.requests.eGovEIS.employeeMaster.RequestInfo;
import org.codehaus.jackson.annotate.JsonProperty;

public class LeaveTypeCreateRequest {

    @JsonProperty("LeaveType")
    private LeaveType[] LeaveType;

    @JsonProperty("RequestInfo")
    private RequestInfo RequestInfo;

    public LeaveType[] getLeaveType() {
        return this.LeaveType;
    }

    public void setLeaveType(LeaveType[] LeaveType) {
        this.LeaveType = LeaveType;
    }

    public RequestInfo getRequestInfo() {
        return this.RequestInfo;
    }

    public void setRequestInfo(RequestInfo RequestInfo) {
        this.RequestInfo = RequestInfo;
    }
}
