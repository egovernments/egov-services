package tests.commonMasters;

import builders.commonMaster.CommonMasterRequestBuilder;
import builders.commonMaster.RequestInfoBuilder;
import com.jayway.restassured.response.Response;
import entities.requests.commonMasters.CommonMasterRequest;
import entities.requests.commonMasters.RequestInfo;
import entities.responses.commonMaster.religion.ReligionResponse;
import entities.responses.login.LoginResponse;
import org.junit.Assert;
import org.testng.annotations.Test;
import resources.CommonMasterResource;
import tests.BaseAPITest;
import utils.*;

import java.io.IOException;

import static data.UserData.ADMIN;
import static data.UserData.NARASAPPA;

public class ReligionTest extends BaseAPITest {

    @Test(groups = {Categories.HR, Categories.SANITY, Categories.PILOT})
    public void religionTest() throws IOException {

        // Login Test
        String sessionId = LoginAndLogoutHelper.loginFromPilotService(ADMIN);

        // Language Search Test
        religionTestMethod(sessionId);
    }

    private void religionTestMethod(String sessionId) throws IOException {
        RequestInfo requestInfo = new RequestInfoBuilder().build();
        CommonMasterRequest commonMasterRequest = new CommonMasterRequestBuilder()
                .withRequestInfo(requestInfo)
                .build();

        String jsonString = RequestHelper.getJsonString(commonMasterRequest);

        Response response = new CommonMasterResource().searchReligionTest(jsonString, sessionId);

        ReligionResponse religionResponse = (ReligionResponse)
                ResponseHelper.getResponseAsObject(response.asString(), ReligionResponse.class);

        Assert.assertTrue(religionResponse.getReligion().length > 0);
        Assert.assertEquals(response.getStatusCode(), 200);

        new APILogger().log("Search Religion Test is Completed --");

        // Logout Test
        pilotLogoutService(sessionId);
    }

}
