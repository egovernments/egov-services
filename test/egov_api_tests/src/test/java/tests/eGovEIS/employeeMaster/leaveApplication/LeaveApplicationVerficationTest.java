package tests.eGovEIS.employeeMaster.leaveApplication;


import builders.eGovEIS.employeeMaster.leaveApplication.CreateLeaveApplicationRequestBuilder;
import builders.eGovEIS.employeeMaster.leaveApplication.RequestInfoBuilder;
import builders.eGovEIS.employeeMaster.leaveApplication.SearchLeaveApplicationRequestBuilder;
import com.jayway.restassured.response.Response;
import entities.requests.eGovEIS.employeeMaster.leaveApplication.CreateLeaveApplicationRequest;
import entities.requests.eGovEIS.employeeMaster.leaveApplication.RequestInfo;
import entities.requests.eGovEIS.employeeMaster.leaveApplication.SearchLeaveApplicationRequest;
import entities.responses.eGovEIS.employeeMaster.LeaveApplication.CreateLeaveApplicationResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import resources.employeeMaster.leaveApplication.LeaveApplicationResource;
import tests.BaseAPITest;
import utils.RequestHelper;
import utils.ResponseHelper;

import java.io.IOException;

public class LeaveApplicationVerficationTest extends BaseAPITest{

  @Test
  public void leaveApplicationTest() throws IOException, InterruptedException {
      //Login Test
      //LoginResponse loginResponse = LoginAndLogoutHelper.login(narasappa);

      //Create a leave application
      String applicationNum = leaveApplicationCreateTestMethod();

      Thread.sleep(10);

      //Search the leave application
      leaveApplicationSearchTestMethod(applicationNum);
  }

  private String leaveApplicationCreateTestMethod() throws IOException {

    CreateLeaveApplicationRequest request = new CreateLeaveApplicationRequestBuilder().build();

    String jsonString = RequestHelper.getJsonString(request);

    Response response = new LeaveApplicationResource().create(jsonString);

    Assert.assertEquals(response.getStatusCode(),200);

    CreateLeaveApplicationResponse response1 = (CreateLeaveApplicationResponse)
                                ResponseHelper.getResponseAsObject(response.asString(),CreateLeaveApplicationResponse.class);

    Assert.assertEquals(request.getLeaveApplication()[0].getReason(),response1.getLeaveApplication()[0].getReason());
    Assert.assertEquals(response1.getLeaveApplication()[0].getStatus(),"APPLIED");

    return response1.getLeaveApplication()[0].getApplicationNumber();
  }

  private void leaveApplicationSearchTestMethod(String appNum) throws IOException {

    System.out.println(appNum);

    SearchLeaveApplicationRequest request = new SearchLeaveApplicationRequestBuilder().build();

    String json = RequestHelper.getJsonString(request);

    Response response = new LeaveApplicationResource().search(json,appNum);

    Assert.assertEquals(response.getStatusCode(),200);
  }
}



