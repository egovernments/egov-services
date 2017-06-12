package builders.lams;

import entities.requests.lams.LamsServiceRequest;
import entities.requests.lams.RequestInfo;

public class LamsServiceRequestBuilder {

    LamsServiceRequest request = new LamsServiceRequest();

    RequestInfo requestInfo = new RequestInfoBuilder().build();

    public LamsServiceRequestBuilder() {
        request.setRequestInfo(requestInfo);
    }

    public LamsServiceRequestBuilder withRequestInfo(RequestInfo requestInfo1) {
        request.setRequestInfo(requestInfo1);
        return this;
    }

    public LamsServiceRequest build() {
        return request;
    }
}
