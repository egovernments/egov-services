package builders.eGovEIS.hrMaster.position.search;

import entities.responses.eGovEIS.hrMaster.position.search.HRMasterPositionSearchRequest;
import entities.responses.eGovEIS.hrMaster.position.search.RequestInfo;

public class HRMasterPositionSearchRequestBuilder {

    HRMasterPositionSearchRequest hRMasterPositionSearchRequest = new HRMasterPositionSearchRequest();
    RequestInfo requestInfo = new RequestInfoBuilder().build();

    public HRMasterPositionSearchRequestBuilder() {
        hRMasterPositionSearchRequest.setRequestInfo(requestInfo);
    }

    public HRMasterPositionSearchRequestBuilder withRequestInfo(RequestInfo RequestInfo) {
        hRMasterPositionSearchRequest.setRequestInfo(RequestInfo);
        return this;
    }

    public HRMasterPositionSearchRequest build() {
        return hRMasterPositionSearchRequest;
    }
}
