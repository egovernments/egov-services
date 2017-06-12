package builders.eGovEIS.employeeMaster.leaveApplication;


import entities.requests.eGovEIS.employeeMaster.leaveApplication.CreateLeaveApplicationRequest;
import entities.requests.eGovEIS.employeeMaster.leaveApplication.LeaveApplication;
import entities.requests.eGovEIS.employeeMaster.leaveApplication.RequestInfo;
import entities.requests.eGovEIS.employeeMaster.leaveApplication.UserInfo;

public class CreateLeaveApplicationRequestBuilder {

  CreateLeaveApplicationRequest request = new CreateLeaveApplicationRequest();
  UserInfo userInfo = new UserInfoBuilder().build();
  RequestInfo requestInfo = new RequestInfoBuilder().withUserInfo(userInfo).build();
  LeaveApplication[] leaveApplications = new LeaveApplication[1];
  LeaveApplication leaveApplication1 = new LeaveApplicationBuilder().build();

  public CreateLeaveApplicationRequestBuilder(){
     request.setRequestInfo(requestInfo);
     leaveApplications[0] = leaveApplication1;
     request.setLeaveApplication(leaveApplications);
  }

  public CreateLeaveApplicationRequest build(){
      return request;
  }
}
