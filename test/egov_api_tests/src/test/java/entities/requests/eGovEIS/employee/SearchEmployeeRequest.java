package entities.requests.eGovEIS.employee;

import org.codehaus.jackson.annotate.JsonProperty;

public class SearchEmployeeRequest {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }
}