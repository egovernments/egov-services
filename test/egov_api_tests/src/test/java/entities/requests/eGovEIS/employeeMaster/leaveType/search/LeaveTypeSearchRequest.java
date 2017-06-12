package entities.requests.eGovEIS.employeeMaster.leaveType.search;

import entities.requests.eGovEIS.employeeMaster.RequestInfo;
import org.codehaus.jackson.annotate.JsonProperty;

public class LeaveTypeSearchRequest {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }
}
