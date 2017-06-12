package tests.userServices;

import builders.userServices.createNoValidate.CreateUserRequestBuilder;
import builders.userServices.createNoValidate.GetUserRequestBuilder;
import builders.userServices.createNoValidate.RequestInfoBuilder;
import builders.userServices.createNoValidate.UserBuilder;
import com.jayway.restassured.response.Response;
import entities.requests.userServices.createNoValidate.CreateUserRequest;
import entities.requests.userServices.createNoValidate.GetUserRequest;
import entities.requests.userServices.createNoValidate.RequestInfo;
import entities.requests.userServices.createNoValidate.User;
import entities.responses.login.LoginResponse;
import entities.responses.userServices.createUser.UserResponse;
import entities.responses.userServices.getUser.GetUserResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import resources.UserServiceResource;
import tests.BaseAPITest;
import utils.*;

import java.io.IOException;

import static data.UserData.NARASAPPA;

public class CreateNoValidateVerificationTest extends BaseAPITest {

    @Test(groups = {Categories.SANITY,Categories.DEV,Categories.USER})
    public void createNoValidateAndGetTest() throws IOException {

        // Login Test
        LoginResponse loginResponse = LoginAndLogoutHelper.login(NARASAPPA);

        // Create a user
        UserResponse create = CreateAUserTest(loginResponse);

        //Get User Details with Id
        getTheNewlyCreatedUser(loginResponse,create,"id");

        //Get User Details with UserName
        getTheNewlyCreatedUser(loginResponse,create,"userName");

        //Update User Details
        UserResponse update =updateTheUserTest(loginResponse,create.getUser()[0].getId());

        //Get User Details with Id
        getTheNewlyCreatedUser(loginResponse,update,"id");

        //Get User Details with UserName
        getTheNewlyCreatedUser(loginResponse,update,"userName");
    }

    private UserResponse updateTheUserTest(LoginResponse loginResponse, int id) throws IOException {

        new APILogger().log("Update user test is started ---");

        RequestInfo requestInfo = new RequestInfoBuilder().withAuthToken(loginResponse.getAccess_token()).build();

        User user = new UserBuilder("").build();

        CreateUserRequest request = new CreateUserRequestBuilder().withRequestInfo(requestInfo).withUser(user).build();

        String json = RequestHelper.getJsonString(request);

        Response response = new UserServiceResource().updateUserDetails(json,id);

        Assert.assertEquals(response.getStatusCode(),200);

        UserResponse response1 = (UserResponse)
                ResponseHelper.getResponseAsObject(response.asString(),UserResponse.class);

        Assert.assertEquals(response1.getUser()[0].getId(),id);

        Assert.assertEquals(request.getUser().getName(),response1.getUser()[0].getName());

        new APILogger().log("Update user test is completed ---");

        return response1;
    }

    private void getTheNewlyCreatedUser(LoginResponse loginResponse, UserResponse create, String searchType) throws IOException {

      new APILogger().log("get user details with search type "+searchType+" is started ---");

      RequestInfo requestInfo = new RequestInfoBuilder("").withAuthToken(loginResponse.getAccess_token()).build();

      GetUserRequest request = new GetUserRequest();

      switch (searchType){
          case "id" :
              int[] ids = new int[1];
              int id = create.getUser()[0].getId();
              ids[0] = id;
              request  = new GetUserRequestBuilder().withRequestInfo(requestInfo).withId(ids).build();
              break;

          case "userName" :
              request  = new GetUserRequestBuilder().withRequestInfo(requestInfo).withUserName(create.getUser()[0].getUserName()).build();
              break;
      }

        String json = RequestHelper.getJsonString(request);

        Response response = new UserServiceResource().getUserDetails(json);

        Assert.assertEquals(response.getStatusCode(),200);

        GetUserResponse response1 = (GetUserResponse)
                ResponseHelper.getResponseAsObject(response.asString(),GetUserResponse.class);

        Assert.assertEquals(response1.getUser()[0].getUserName(),create.getUser()[0].getUserName());

        new APILogger().log("get user details with search type "+searchType+" is completed ---");
    }

    private UserResponse CreateAUserTest(LoginResponse loginResponse) throws IOException {

        new APILogger().log("Create user test is started ---");

        RequestInfo requestInfo = new RequestInfoBuilder().withAuthToken(loginResponse.getAccess_token()).build();

        User user = new UserBuilder().withUserName("Test_"+get3DigitRandomInt()+get3DigitRandomInt()).build();

        CreateUserRequest request = new CreateUserRequestBuilder().withRequestInfo(requestInfo).withUser(user).build();

        String json = RequestHelper.getJsonString(request);

        Response response = new UserServiceResource().createUserNoValidate(json);

        Assert.assertEquals(response.getStatusCode(),200);

        UserResponse response1 = (UserResponse)
                ResponseHelper.getResponseAsObject(response.asString(),UserResponse.class);

        Assert.assertEquals(request.getUser().getUserName(),response1.getUser()[0].getUserName());

        new APILogger().log("Create user test is completed ---");

        return response1;
    }
}
