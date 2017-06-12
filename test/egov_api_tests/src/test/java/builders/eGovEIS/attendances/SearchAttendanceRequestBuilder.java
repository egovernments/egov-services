package builders.eGovEIS.attendances;

import entities.requests.eGovEIS.RequestInfo;
import entities.requests.eGovEIS.attendances.SearchAttendanceRequest;

public final class SearchAttendanceRequestBuilder {

    SearchAttendanceRequest searchAttendanceRequest = new SearchAttendanceRequest();
    RequestInfo requestInfo = new RequestInfoBuilder("Search").build1();

    public SearchAttendanceRequestBuilder() {
        searchAttendanceRequest.setRequestInfo(requestInfo);
    }

    public SearchAttendanceRequestBuilder withRequestInfo(RequestInfo requestInfo) {
        searchAttendanceRequest.setRequestInfo(requestInfo);
        return this;
    }

    public SearchAttendanceRequest build() {
        return searchAttendanceRequest;
    }
}
