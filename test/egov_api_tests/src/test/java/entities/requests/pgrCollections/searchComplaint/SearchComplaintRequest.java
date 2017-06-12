package entities.requests.pgrCollections.searchComplaint;

import org.codehaus.jackson.annotate.JsonProperty;

public class SearchComplaintRequest {

    @JsonProperty("RequestInfo")
    RequestInfo requestInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }
}
