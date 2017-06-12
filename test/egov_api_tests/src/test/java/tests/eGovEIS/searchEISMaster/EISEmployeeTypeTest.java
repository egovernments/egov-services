package tests.eGovEIS.searchEISMaster;

import builders.eGovEIS.searchEISMaster.RequestInfoBuilder;
import builders.eGovEIS.searchEISMaster.SearchEmployeeMasterRequestBuilder;
import com.jayway.restassured.response.Response;
import entities.requests.eGovEIS.searchEISMaster.RequestInfo;
import entities.requests.eGovEIS.searchEISMaster.SearchEmployeeMasterRequest;
import entities.responses.eGovEIS.searchEISMasters.employeeType.SearchEmployeeTypeResponse;
import entities.responses.login.LoginResponse;
import org.junit.Assert;
import org.testng.annotations.Test;
import resources.searchEISMaster.EISMasterResource;
import tests.BaseAPITest;
import utils.*;

import java.io.IOException;

import static data.UserData.NARASAPPA;

public class EISEmployeeTypeTest extends BaseAPITest {

    @Test(groups = {Categories.HR, Categories.SANITY})
    public void searchEmployeeTypeTest() throws IOException {

        // Login Test
        LoginResponse loginResponse = LoginAndLogoutHelper.login(NARASAPPA);

        // Search Designation Test
        searchEmployeeTypeTestMethod(loginResponse);
    }

    private void searchEmployeeTypeTestMethod(LoginResponse loginResponse) throws IOException {

        RequestInfo requestInfo = new RequestInfoBuilder()
                .withAuthToken(loginResponse.getAccess_token())
                .build();

        SearchEmployeeMasterRequest searchEmployeeMasterRequest = new SearchEmployeeMasterRequestBuilder()
                .withRequestInfo(requestInfo)
                .build();

        Response response = new EISMasterResource().
                searchEmployeeType(RequestHelper.getJsonString(searchEmployeeMasterRequest));

        SearchEmployeeTypeResponse searchEmployeeTypeResponse = (SearchEmployeeTypeResponse)
                ResponseHelper.getResponseAsObject(response.asString(), SearchEmployeeTypeResponse.class);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(searchEmployeeTypeResponse.getEmployeeType().length, 4);

        new APILogger().log("Search createEmployee Test is Completed--");
    }
}
