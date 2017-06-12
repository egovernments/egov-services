package entities.requests.eGovEIS.searchEISMaster;

import org.codehaus.jackson.annotate.JsonProperty;

public class SearchEmployeeMasterRequest {
    @JsonProperty("RequestInfo")
    private RequestInfo RequestInfo;

    public RequestInfo getRequestInfo() {
        return RequestInfo;
    }

    public void setRequestInfo(RequestInfo RequestInfo) {
        this.RequestInfo = RequestInfo;
    }
}
