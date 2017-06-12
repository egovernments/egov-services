package entities.responses.eGovEIS.searchEmployeeLeave;

import org.codehaus.jackson.annotate.JsonProperty;

public class SearchEmployeeLeaveResponse {

    private ResponseInfo ResponseInfo;
    @JsonProperty("LeaveType")
    private LeaveType[] LeaveType;

    public ResponseInfo getResponseInfo() {
        return this.ResponseInfo;
    }

    public void setResponseInfo(ResponseInfo ResponseInfo) {
        this.ResponseInfo = ResponseInfo;
    }

    public LeaveType[] getLeaveType() {
        return this.LeaveType;
    }

    public void setLeaveType(LeaveType[] LeaveType) {
        this.LeaveType = LeaveType;
    }
}
