package entities.requests.pgrCollections.receivingCenters;

import org.codehaus.jackson.annotate.JsonProperty;

public class ReceivingCentersRequest {

    @JsonProperty("RequestInfo")
    private RequestInfo RequestInfo;

    public RequestInfo getRequestInfo() {
        return this.RequestInfo;
    }

    public void setRequestInfo(RequestInfo RequestInfo) {
        this.RequestInfo = RequestInfo;
    }
}
