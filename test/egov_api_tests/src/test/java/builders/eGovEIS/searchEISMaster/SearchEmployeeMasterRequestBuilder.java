package builders.eGovEIS.searchEISMaster;

import entities.requests.eGovEIS.searchEISMaster.RequestInfo;
import entities.requests.eGovEIS.searchEISMaster.SearchEmployeeMasterRequest;

public final class SearchEmployeeMasterRequestBuilder {

    SearchEmployeeMasterRequest searchEmployeeMasterRequest = new SearchEmployeeMasterRequest();
    RequestInfo requestInfo = new RequestInfo();

    public SearchEmployeeMasterRequestBuilder() {
        searchEmployeeMasterRequest.setRequestInfo(requestInfo);
    }

    public SearchEmployeeMasterRequestBuilder withRequestInfo(RequestInfo RequestInfo) {
        searchEmployeeMasterRequest.setRequestInfo(RequestInfo);
        return this;
    }

    public SearchEmployeeMasterRequest build() {
        return searchEmployeeMasterRequest;
    }
}
