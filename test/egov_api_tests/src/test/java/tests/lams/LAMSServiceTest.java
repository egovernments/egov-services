package tests.lams;

import builders.lams.LamsServiceRequestBuilder;
import builders.lams.RequestInfoBuilder;
import com.jayway.restassured.response.Response;
import entities.requests.lams.LamsServiceRequest;
import entities.requests.lams.RequestInfo;
import entities.responses.lams.LamsServiceSearchResponse;
import entities.responses.login.LoginResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import resources.LAMSServiceResource;
import tests.BaseAPITest;
import utils.*;

import java.io.IOException;

import static data.UserData.NARASAPPA;

public class LAMSServiceTest extends BaseAPITest {

    @Test(groups = {Categories.LAMS, Categories.SANITY})
    public void lamsServiceSearchTest() throws IOException {

        // Login Test
        LoginResponse loginResponse = LoginAndLogoutHelper.login(NARASAPPA);

        // LAMS Service Search Test
        lamsServiceTestMethod(loginResponse);
    }

    private void lamsServiceTestMethod(LoginResponse loginResponse) throws IOException {
        RequestInfo requestInfo = new RequestInfoBuilder().withAuthToken(loginResponse.getAccess_token()).build();
        LamsServiceRequest request = new LamsServiceRequestBuilder().withRequestInfo(requestInfo).build();

        String jsonString = RequestHelper.getJsonString(request);

        System.out.println(jsonString);

        Response response = new LAMSServiceResource().lamsServiceSearch(jsonString);

        Assert.assertEquals(response.getStatusCode(), 200);

        LamsServiceSearchResponse lamsServiceSearchResponse = (LamsServiceSearchResponse)
                ResponseHelper.getResponseAsObject(response.asString(), LamsServiceSearchResponse.class);

        Assert.assertEquals(lamsServiceSearchResponse.getResposneInfo().getStatus(), "successful");

        new APILogger().log("LAMS service search request is Completed -- ");
    }
}
