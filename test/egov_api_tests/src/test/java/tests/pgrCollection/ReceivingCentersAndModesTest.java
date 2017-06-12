package tests.pgrCollection;

import builders.pgrCollection.receivingCenters.ReceivingCentersRequestBuilder;
import builders.pgrCollection.receivingCenters.RequestInfoBuilder;
import com.jayway.restassured.response.Response;
import entities.requests.pgrCollections.receivingCenters.ReceivingCentersRequest;
import entities.requests.pgrCollections.receivingCenters.RequestInfo;
import entities.responses.login.LoginResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import resources.PGRResource;
import tests.BaseAPITest;
import utils.APILogger;
import utils.Categories;
import utils.LoginAndLogoutHelper;
import utils.RequestHelper;

import java.io.IOException;

import static data.UserData.NARASAPPA;

public class ReceivingCentersAndModesTest extends BaseAPITest {

    @Test(groups = {Categories.PGR, Categories.SANITY, Categories.DEV})
    public void receivingCentersTest()throws IOException{

        //Login Test
        LoginResponse loginResponse = LoginAndLogoutHelper.login(NARASAPPA);

        //AllReceivingCenters
        getAllReceivingCentersTest(loginResponse);

        //ReceivingCenterById
        getReceivingCenterByIdTest(loginResponse);

        //ReceivingMode
        getAllReceivingModesTest(loginResponse);
    }

    private void getAllReceivingModesTest(LoginResponse loginResponse) {

        new APILogger().log("Receiving modes test is started ---");

        RequestInfo requestInfo = new RequestInfoBuilder().withAuthToken(loginResponse.getAccess_token()).build();

        ReceivingCentersRequest request = new ReceivingCentersRequestBuilder().withRequestInfo(requestInfo).build();

        String json = RequestHelper.getJsonString(request);

        Response response = new PGRResource().getReceivingModes(json);

        Assert.assertEquals(response.getStatusCode(),200);

        new APILogger().log("Receiving modes test is completed ---");
    }

    private void getAllReceivingCentersTest(LoginResponse loginResponse){

        new APILogger().log("All Receiving centers test is started ---");

        RequestInfo requestInfo = new RequestInfoBuilder().withAuthToken(loginResponse.getAccess_token()).build();

        ReceivingCentersRequest request = new ReceivingCentersRequestBuilder().withRequestInfo(requestInfo).build();

        String json = RequestHelper.getJsonString(request);

        Response response = new PGRResource().getReceivingCenter(json);

        Assert.assertEquals(response.getStatusCode(),200);

        new APILogger().log("All Receiving Centers test is Completed ---");
    }

    private void getReceivingCenterByIdTest(LoginResponse loginResponse){

        new APILogger().log("Receiving Centers by Id test is Started ---");

        RequestInfo requestInfo = new RequestInfoBuilder().withAuthToken(loginResponse.getAccess_token()).build();

        ReceivingCentersRequest request = new ReceivingCentersRequestBuilder().withRequestInfo(requestInfo).build();

        String json = RequestHelper.getJsonString(request);

        Response response = new PGRResource().getReceivingCenterById(json);

        Assert.assertEquals(response.getStatusCode(),200);

        new APILogger().log("Receiving Centers by Id test is Completed ---");
    }

}
