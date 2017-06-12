package tests.eGovEIS.searchEISMaster;

import builders.eGovEIS.searchEISMaster.RequestInfoBuilder;
import builders.eGovEIS.searchEISMaster.SearchEmployeeMasterRequestBuilder;
import com.jayway.restassured.response.Response;
import entities.requests.eGovEIS.searchEISMaster.RequestInfo;
import entities.requests.eGovEIS.searchEISMaster.SearchEmployeeMasterRequest;
import entities.responses.eGovEIS.searchEISMasters.hrConfigurations.SearchHRConfigurationsResponse;
import entities.responses.login.LoginResponse;
import org.junit.Assert;
import org.testng.annotations.Test;
import resources.searchEISMaster.EISMasterResource;
import utils.*;

import java.io.IOException;

import static data.UserData.NARASAPPA;

public class EISHRConfigurationsTest {

    @Test(groups = {Categories.HR, Categories.SANITY})
    public void searchHRConfigurationsTest() throws IOException {

        // Login Test
        LoginResponse loginResponse = LoginAndLogoutHelper.login(NARASAPPA);

        // Search HrConfigurations Test
        searchHRConfigurationsTestMethod(loginResponse);
    }

    private void searchHRConfigurationsTestMethod(LoginResponse loginResponse) throws IOException {

        RequestInfo requestInfo = new RequestInfoBuilder()
                .withAuthToken(loginResponse.getAccess_token())
                .build();

        SearchEmployeeMasterRequest searchEmployeeMasterRequest = new SearchEmployeeMasterRequestBuilder()
                .withRequestInfo(requestInfo)
                .build();

        Response response = new EISMasterResource().
                searchHRConfigurationsType(RequestHelper.getJsonString(searchEmployeeMasterRequest));

        SearchHRConfigurationsResponse searchHRConfigurationsResponse = (SearchHRConfigurationsResponse)
                ResponseHelper.getResponseAsObject(response.asString(), SearchHRConfigurationsResponse.class);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(searchHRConfigurationsResponse.getHRConfiguration().getWeekly_holidays()[0].contains("5-day week"));

        new APILogger().log("Search HR Configurations Test is Completed--");
    }
}
