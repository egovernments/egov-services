package tests.eGovEIS.searchEISMaster;

import builders.eGovEIS.searchEISMaster.RequestInfoBuilder;
import builders.eGovEIS.searchEISMaster.SearchEmployeeMasterRequestBuilder;
import com.jayway.restassured.response.Response;
import entities.requests.eGovEIS.searchEISMaster.RequestInfo;
import entities.requests.eGovEIS.searchEISMaster.SearchEmployeeMasterRequest;
import entities.responses.eGovEIS.searchEISMasters.employeeGroup.SearchEmployeeGroupResponse;
import entities.responses.login.LoginResponse;
import org.junit.Assert;
import org.testng.annotations.Test;
import resources.searchEISMaster.EISMasterResource;
import utils.*;

import java.io.IOException;

import static data.UserData.NARASAPPA;

public class EISEmployeeGroupTest {

    @Test(groups = {Categories.HR, Categories.SANITY})
    public void searchEmployeeGroupTest() throws IOException {

        // Login Test
        LoginResponse loginResponse = LoginAndLogoutHelper.login(NARASAPPA);

        // Search Designation Test
        searchEmployeeGroupTestMethod(loginResponse);
    }

    private void searchEmployeeGroupTestMethod(LoginResponse loginResponse) throws IOException {

        RequestInfo requestInfo = new RequestInfoBuilder()
                .withAuthToken(loginResponse.getAccess_token())
                .build();

        SearchEmployeeMasterRequest searchEmployeeMasterRequest = new SearchEmployeeMasterRequestBuilder()
                .withRequestInfo(requestInfo)
                .build();

        Response response = new EISMasterResource().
                searchEmployeeGroup(RequestHelper.getJsonString(searchEmployeeMasterRequest));

        SearchEmployeeGroupResponse searchEmployeeGroupResponse = (SearchEmployeeGroupResponse)
                ResponseHelper.getResponseAsObject(response.asString(), SearchEmployeeGroupResponse.class);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(searchEmployeeGroupResponse.getGroup().length, 3);

        new APILogger().log("Search createEmployee Group Test is Completed--");
    }
}
