package builders.eGovEIS.leaveManagement.search;

import entities.requests.eGovEIS.leaveManagement.search.RequestInfo;
import entities.requests.eGovEIS.leaveManagement.search.SearchOpeningBalanceRequest;

public final class SearchOpeningBalanceRequestBuilder {

    SearchOpeningBalanceRequest searchOpeningBalanceRequest = new SearchOpeningBalanceRequest();
    RequestInfo requestInfo = new RequestInfo();

    public SearchOpeningBalanceRequestBuilder() {
        searchOpeningBalanceRequest.setRequestInfo(requestInfo);
    }

    public static SearchOpeningBalanceRequestBuilder aSearchOpeningBalanceRequest() {
        return new SearchOpeningBalanceRequestBuilder();
    }

    public SearchOpeningBalanceRequestBuilder withRequestInfo(RequestInfo requestInfo) {
        searchOpeningBalanceRequest.setRequestInfo(requestInfo);
        return this;
    }

    public SearchOpeningBalanceRequest build() {
        return searchOpeningBalanceRequest;
    }
}
