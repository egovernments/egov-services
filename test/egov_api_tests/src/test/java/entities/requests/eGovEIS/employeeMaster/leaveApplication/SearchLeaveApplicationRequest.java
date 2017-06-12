package entities.requests.eGovEIS.employeeMaster.leaveApplication;

import org.codehaus.jackson.annotate.JsonProperty;

public class SearchLeaveApplicationRequest {

    @JsonProperty("RequestInfo")
    RequestInfo requestInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }
}
