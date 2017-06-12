package entities.requests.userServices.userDetails;

import org.codehaus.jackson.annotate.JsonProperty;

public class UserDetailsRequest {

    @JsonProperty("RequestInfo")
    private RequestInfo RequestInfo;

    public RequestInfo getRequestInfo() {
        return this.RequestInfo;
    }

    public void setRequestInfo(RequestInfo RequestInfo) {
        this.RequestInfo = RequestInfo;
    }
}
