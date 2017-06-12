package builders.commonMaster;

import entities.requests.commonMasters.CommonMasterRequest;
import entities.requests.commonMasters.RequestInfo;

public final class CommonMasterRequestBuilder {

    CommonMasterRequest commonMasterRequest = new CommonMasterRequest();
    RequestInfo requestInfo = new RequestInfo();

    public CommonMasterRequestBuilder() {
        commonMasterRequest.setRequestInfo(requestInfo);
    }

    public CommonMasterRequestBuilder withRequestInfo(RequestInfo RequestInfo) {
        commonMasterRequest.setRequestInfo(RequestInfo);
        return this;
    }

    public CommonMasterRequest build() {
        return commonMasterRequest;
    }
}
