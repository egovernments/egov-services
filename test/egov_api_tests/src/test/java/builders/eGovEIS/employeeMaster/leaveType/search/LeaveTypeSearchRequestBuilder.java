package builders.eGovEIS.employeeMaster.leaveType.search;

import entities.requests.eGovEIS.employeeMaster.RequestInfo;
import entities.requests.eGovEIS.employeeMaster.leaveType.search.LeaveTypeSearchRequest;

public final class LeaveTypeSearchRequestBuilder {

    LeaveTypeSearchRequest leaveTypeSearchRequest = new LeaveTypeSearchRequest();
    RequestInfo requestInfo = new RequestInfo();

    public LeaveTypeSearchRequestBuilder() {
        leaveTypeSearchRequest.setRequestInfo(requestInfo);
    }

    public LeaveTypeSearchRequestBuilder withRequestInfo(RequestInfo requestInfo) {
        leaveTypeSearchRequest.setRequestInfo(requestInfo);
        return this;
    }

    public LeaveTypeSearchRequest build() {
        return leaveTypeSearchRequest;
    }
}
