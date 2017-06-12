package entities.requests.eGovEIS.hrMaster.designation.search;

import entities.requests.eGovEIS.hrMaster.RequestInfo;
import org.codehaus.jackson.annotate.JsonProperty;

public class HRMasterDesignationSearchRequest {

    @JsonProperty("ResponseInfo")
    private RequestInfo requestInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }
}
