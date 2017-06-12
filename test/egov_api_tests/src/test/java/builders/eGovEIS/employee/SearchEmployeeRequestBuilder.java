package builders.eGovEIS.employee;

import entities.requests.eGovEIS.employee.RequestInfo;
import entities.requests.eGovEIS.employee.SearchEmployeeRequest;

public class SearchEmployeeRequestBuilder {

    SearchEmployeeRequest request = new SearchEmployeeRequest();
    RequestInfo requestInfo = new RequestInfoBuilder().build();

    public SearchEmployeeRequestBuilder() {
        request.setRequestInfo(requestInfo);
    }

    public SearchEmployeeRequestBuilder withRequestInfo(RequestInfo requestInfo1) {
        request.setRequestInfo(requestInfo1);
        return this;
    }

    public SearchEmployeeRequest build() {
        return request;
    }
}
