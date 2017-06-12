package tests.commonMasters;

import builders.commonMaster.CommonMasterRequestBuilder;
import builders.commonMaster.RequestInfoBuilder;
import com.jayway.restassured.response.Response;
import entities.requests.commonMasters.CommonMasterRequest;
import entities.requests.commonMasters.RequestInfo;
import entities.responses.commonMaster.department.DepartmentResponse;
import entities.responses.login.LoginResponse;
import org.junit.Assert;
import org.testng.annotations.Test;
import resources.CommonMasterResource;
import tests.BaseAPITest;
import utils.*;

import java.io.IOException;

import static data.UserData.ADMIN;
import static data.UserData.NARASAPPA;

public class DepartmentTest extends BaseAPITest {

    @Test(groups = {Categories.HR, Categories.SANITY, Categories.PILOT})
    public void departmentTest() throws IOException {

        // Login Test
        String sessionId = LoginAndLogoutHelper.loginFromPilotService(ADMIN);

        // Search Department Test
        departmentTestMethod(sessionId);
    }

    private void departmentTestMethod(String sessionId) throws IOException {
        RequestInfo requestInfo = new RequestInfoBuilder().build();
        CommonMasterRequest commonMasterRequest = new CommonMasterRequestBuilder()
                .withRequestInfo(requestInfo)
                .build();

        String jsonString = RequestHelper.getJsonString(commonMasterRequest);

        Response response = new CommonMasterResource().searchDepartmentTest(jsonString, sessionId);

        DepartmentResponse departmentResponse = (DepartmentResponse)
                ResponseHelper.getResponseAsObject(response.asString(), DepartmentResponse.class);

        Assert.assertTrue(departmentResponse.getDepartment().length > 0);
        Assert.assertEquals(response.getStatusCode(), 200);

        new APILogger().log("Search Department Test is Completed --");

        // Logout Test
        pilotLogoutService(sessionId);
    }
}
