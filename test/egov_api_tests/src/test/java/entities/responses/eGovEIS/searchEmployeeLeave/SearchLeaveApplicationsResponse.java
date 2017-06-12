package entities.responses.eGovEIS.searchEmployeeLeave;

import org.codehaus.jackson.annotate.JsonProperty;

public class SearchLeaveApplicationsResponse {
    @JsonProperty("ResponseInfo")
    private ResponseInfo ResponseInfo;

    @JsonProperty("LeaveApplication")
    private LeaveApplication[] LeaveApplication;

    public ResponseInfo getResponseInfo() {
        return ResponseInfo;
    }

    public void setResponseInfo(ResponseInfo ResponseInfo) {
        this.ResponseInfo = ResponseInfo;
    }

    public LeaveApplication[] getLeaveApplication() {
        return LeaveApplication;
    }

    public void setLeaveApplication(LeaveApplication[] LeaveApplication) {
        this.LeaveApplication = LeaveApplication;
    }
}
