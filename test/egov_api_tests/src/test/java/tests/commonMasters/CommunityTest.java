package tests.commonMasters;

import builders.commonMaster.CommonMasterRequestBuilder;
import builders.commonMaster.RequestInfoBuilder;
import com.jayway.restassured.response.Response;
import entities.requests.commonMasters.CommonMasterRequest;
import entities.requests.commonMasters.RequestInfo;
import entities.responses.commonMaster.community.CommunityResponse;
import entities.responses.login.LoginResponse;
import org.junit.Assert;
import org.testng.annotations.Test;
import resources.CommonMasterResource;
import tests.BaseAPITest;
import utils.*;

import java.io.IOException;

import static data.UserData.ADMIN;
import static data.UserData.NARASAPPA;

public class CommunityTest extends BaseAPITest {

    @Test(groups = {Categories.HR, Categories.SANITY, Categories.PILOT})
    public void communityTest() throws IOException {

        // Login Test
        String sessionId = LoginAndLogoutHelper.loginFromPilotService(ADMIN);

        // Search Department Test
        communityTestMethod(sessionId);
    }

    private void communityTestMethod(String sessionId) throws IOException {
        RequestInfo requestInfo = new RequestInfoBuilder().build();
        CommonMasterRequest commonMasterRequest = new CommonMasterRequestBuilder()
                .withRequestInfo(requestInfo)
                .build();

        String jsonString = RequestHelper.getJsonString(commonMasterRequest);

        Response response = new CommonMasterResource().searchCommunityTest(jsonString, sessionId);
        CommunityResponse communityResponse = (CommunityResponse)
                ResponseHelper.getResponseAsObject(response.asString(), CommunityResponse.class);

        Assert.assertTrue(communityResponse.getCommunity().length >= 0);
        Assert.assertEquals(response.getStatusCode(), 200);

        new APILogger().log("Search Community Test is Completed --");

        // Logout Test
        pilotLogoutService(sessionId);
    }
}
