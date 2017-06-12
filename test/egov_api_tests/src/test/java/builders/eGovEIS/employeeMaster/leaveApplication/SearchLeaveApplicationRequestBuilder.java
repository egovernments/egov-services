package builders.eGovEIS.employeeMaster.leaveApplication;

import entities.requests.eGovEIS.employeeMaster.leaveApplication.RequestInfo;
import entities.requests.eGovEIS.employeeMaster.leaveApplication.SearchLeaveApplicationRequest;

public class SearchLeaveApplicationRequestBuilder {

    SearchLeaveApplicationRequest request = new SearchLeaveApplicationRequest();
    RequestInfo requestInfo = new RequestInfoBuilder().build();

    public SearchLeaveApplicationRequestBuilder(){
        request.setRequestInfo(requestInfo);
    }

    public SearchLeaveApplicationRequest build(){
        return request;
    }
}
