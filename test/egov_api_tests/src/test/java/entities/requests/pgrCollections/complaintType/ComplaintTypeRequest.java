package entities.requests.pgrCollections.complaintType;

import org.codehaus.jackson.annotate.JsonProperty;

public class ComplaintTypeRequest {

    @JsonProperty("RequestInfo")
    RequestInfo requestInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }
}
