package entities.responses.eGovEIS.employeeMaster.create;

import entities.responses.eGovEIS.employeeMaster.ResponseInfo;
import org.codehaus.jackson.annotate.JsonProperty;

public class LeaveTypeResponse {

    @JsonProperty("LeaveType")
    private LeaveType[] LeaveType;

    @JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo;

    public LeaveType[] getLeaveType() {
        return this.LeaveType;
    }

    public void setLeaveType(LeaveType[] LeaveType) {
        this.LeaveType = LeaveType;
    }

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }
}
