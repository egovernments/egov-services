package builders.eGovEIS.employeeLeave;


import entities.requests.eGovEIS.employeeLeave.RequestInfo;
import entities.requests.eGovEIS.employeeLeave.SearchEmployeeLeaveRequest;

public class SearchEmployeeLeaveRequestBuilder {

    SearchEmployeeLeaveRequest request = new SearchEmployeeLeaveRequest();

    public SearchEmployeeLeaveRequestBuilder() {
    }

    public SearchEmployeeLeaveRequestBuilder withRequestInfo(RequestInfo requestInfo) {
        request.setRequestInfo(requestInfo);
        return this;
    }

    public SearchEmployeeLeaveRequest build() {
        return request;
    }
}
