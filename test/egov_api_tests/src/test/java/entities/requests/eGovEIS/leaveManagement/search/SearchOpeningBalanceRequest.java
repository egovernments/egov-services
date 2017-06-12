package entities.requests.eGovEIS.leaveManagement.search;

import org.codehaus.jackson.annotate.JsonProperty;

public class SearchOpeningBalanceRequest {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }
}
