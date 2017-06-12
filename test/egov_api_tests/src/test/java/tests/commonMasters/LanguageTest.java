package tests.commonMasters;

import builders.commonMaster.CommonMasterRequestBuilder;
import builders.commonMaster.RequestInfoBuilder;
import com.jayway.restassured.response.Response;
import entities.requests.commonMasters.CommonMasterRequest;
import entities.requests.commonMasters.RequestInfo;
import entities.responses.commonMaster.language.LanguageResponse;
import entities.responses.login.LoginResponse;
import org.junit.Assert;
import org.testng.annotations.Test;
import resources.CommonMasterResource;
import tests.BaseAPITest;
import utils.*;

import java.io.IOException;

import static data.UserData.ADMIN;
import static data.UserData.NARASAPPA;

public class LanguageTest extends BaseAPITest {

    @Test(groups = {Categories.HR, Categories.SANITY, Categories.PILOT})
    public void languageTest() throws IOException {

        // Login Test
        String sessionId = LoginAndLogoutHelper.loginFromPilotService(ADMIN);

        // Language Search Test
        languageTestMethod(sessionId);
    }

    private void languageTestMethod(String sessionId) throws IOException {
        RequestInfo requestInfo = new RequestInfoBuilder().build();
        CommonMasterRequest commonMasterRequest = new CommonMasterRequestBuilder()
                .withRequestInfo(requestInfo)
                .build();

        String jsonString = RequestHelper.getJsonString(commonMasterRequest);

        Response response = new CommonMasterResource().searchLanguageTest(jsonString, sessionId);

        LanguageResponse languageResponse = (LanguageResponse)
                ResponseHelper.getResponseAsObject(response.asString(), LanguageResponse.class);

        Assert.assertTrue(languageResponse.getLanguage().length > 0);
        Assert.assertEquals(response.getStatusCode(), 200);

        new APILogger().log("Search Language Test is Completed --");

        // Logout Test
        pilotLogoutService(sessionId);
    }
}
