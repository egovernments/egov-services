package tests.userServices;

import builders.userServices.userDetails.RequestInfoBuilder;
import builders.userServices.userDetails.UserDetailsRequestBuilder;
import com.jayway.restassured.response.Response;
import entities.requests.userServices.userDetails.RequestInfo;
import entities.requests.userServices.userDetails.UserDetailsRequest;
import entities.responses.login.LoginResponse;
import entities.responses.userServices.userDetails.UserDetailsResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import resources.UserServiceResource;
import tests.BaseAPITest;
import utils.*;

import java.io.IOException;

import static data.UserData.NARASAPPA;

public class userDetailsVerificationTest extends BaseAPITest {

   @Test(groups = {Categories.SANITY,Categories.DEV,Categories.USER})
   public void userDetailsTest()throws IOException{

       // Login Test
       LoginResponse loginResponse = LoginAndLogoutHelper.login(NARASAPPA);

       //User Details Test
       verifyUserDetails(loginResponse);

   }

    private void verifyUserDetails(LoginResponse loginResponse) throws IOException {

        new APILogger().log("Verify User Details Test is Started ---");

        RequestInfo requestInfo = new RequestInfoBuilder().withAuthToken(loginResponse.getAccess_token()).build();

        UserDetailsRequest request = new UserDetailsRequestBuilder().withRequestInfo(requestInfo).build();

        String json = RequestHelper.getJsonString(request);

        Response response = new UserServiceResource().getLoginUserDetails(json,loginResponse.getAccess_token());

        Assert.assertEquals(response.getStatusCode(),200);

        UserDetailsResponse response1 = (UserDetailsResponse)
                      ResponseHelper.getResponseAsObject(response.asString(),UserDetailsResponse.class);

        Assert.assertEquals(loginResponse.getUserRequest().getId(),response1.getId());
        Assert.assertEquals(loginResponse.getUserRequest().getName(),response1.getName());
        Assert.assertEquals(loginResponse.getUserRequest().getUserName(),response1.getUserName());
        Assert.assertEquals(loginResponse.getUserRequest().getMobileNumber(),response1.getMobileNumber());
        Assert.assertEquals(loginResponse.getUserRequest().getEmailId(),response1.getEmailId());

        new APILogger().log("Verify User Details Test is Completed ---");
    }
}
