package entities.requests.eGovEIS.employeeLeave;


import org.codehaus.jackson.annotate.JsonProperty;

public class SearchEmployeeLeaveRequest {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }
}
