package utils;


import builders.login.LoginRequestBuilder;
import builders.login.LoginRequestForPilotServiceBuilder;
import builders.logout.LogoutRequestBuilder;
import builders.logout.RequestInfoBuilder;
import com.jayway.restassured.response.Response;
import entities.requests.login.LoginRequest;
import entities.requests.login.LoginRequestForPilotService;
import entities.requests.logout.LogoutRequest;
import entities.requests.logout.RequestInfo;
import entities.responses.login.LoginResponse;
import entities.responses.logout.LogoutResponse;
import org.testng.Assert;
import resources.LoginResource;

import java.io.IOException;
import java.util.Map;

public class LoginAndLogoutHelper {

    public static LoginResponse login(String username) throws IOException {
        LoginRequest request = new LoginRequestBuilder().withUsername(username).build();

        Map jsonString = RequestHelper.asMap(request);

        Response response = new LoginResource().login(jsonString);
        LoginResponse loginResponse = (LoginResponse)
                ResponseHelper.getResponseAsObject(response.asString(), LoginResponse.class);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(loginResponse.getUserRequest().getUserName(), username);

        new APILogger().log("Login Test is Completed -- ");
        return loginResponse;
    }

    public static void logout(LoginResponse loginResponse) throws IOException {

        RequestInfo requestInfo = new RequestInfoBuilder()
                .withAuthToken(loginResponse.getAccess_token())
                .build();
        LogoutRequest logoutRequest = new LogoutRequestBuilder().withRequestInfo(requestInfo).build();

        Response response1 = new LoginResource().logout(RequestHelper.getJsonString(logoutRequest), loginResponse.getAccess_token());
        LogoutResponse logoutResponse = (LogoutResponse)
                ResponseHelper.getResponseAsObject(response1.asString(), LogoutResponse.class);

        Assert.assertEquals(response1.getStatusCode(), 200);
        Assert.assertEquals(logoutResponse.getStatus(), "Logout successfully");

        new APILogger().log("Logout Test is Completed --");

    }

    public static String loginFromPilotService(String username){
        Response baseAPIResponse = new LoginResource().getSessionIdFromPilotBaseAPI();
        String sessionIdFromBaseAPI = baseAPIResponse.getCookie("SESSIONID");

        LoginRequestForPilotService loginRequestForPilotService = new LoginRequestForPilotServiceBuilder()
                .withJ_username(username).build();
        Response loginFromPilotServiceResponse = new LoginResource()
                .loginFromPilotService(sessionIdFromBaseAPI , RequestHelper.asMap(loginRequestForPilotService));
        return loginFromPilotServiceResponse.getCookie("SESSIONID");
    }

    public static void logoutFromPilotService(String sessionIdFromLoginAPI){
        new LoginResource().logoutFromPilotService(sessionIdFromLoginAPI);
    }
}
