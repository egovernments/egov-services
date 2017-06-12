package tests.eGovEIS.searchEISMaster;

import builders.eGovEIS.searchEISMaster.RequestInfoBuilder;
import builders.eGovEIS.searchEISMaster.SearchEmployeeMasterRequestBuilder;
import com.jayway.restassured.response.Response;
import entities.requests.eGovEIS.searchEISMaster.RequestInfo;
import entities.requests.eGovEIS.searchEISMaster.SearchEmployeeMasterRequest;
import entities.responses.eGovEIS.searchEISMasters.designationType.SearchDesignationResponse;
import entities.responses.login.LoginResponse;
import org.junit.Assert;
import org.testng.annotations.Test;
import resources.searchEISMaster.EISMasterResource;
import tests.BaseAPITest;
import utils.*;

import java.io.IOException;

import static data.UserData.NARASAPPA;

public class EISDesignationsTest extends BaseAPITest {

    @Test(groups = {Categories.HR, Categories.SANITY})
    public void searchDesignationTest() throws IOException {

        // Login Test
        LoginResponse loginResponse = LoginAndLogoutHelper.login(NARASAPPA);

        // Search Designation Test
        searchDesignationTestMethod(loginResponse);
    }

    private void searchDesignationTestMethod(LoginResponse loginResponse) throws IOException {

        RequestInfo requestInfo = new RequestInfoBuilder()
                .withAuthToken(loginResponse.getAccess_token())
                .build();

        SearchEmployeeMasterRequest searchEmployeeMasterRequest = new SearchEmployeeMasterRequestBuilder()
                .withRequestInfo(requestInfo)
                .build();

        Response response = new EISMasterResource().
                searchDesignationType(RequestHelper.getJsonString(searchEmployeeMasterRequest));

        SearchDesignationResponse searchEmployeeTypeResponse = (SearchDesignationResponse)
                ResponseHelper.getResponseAsObject(response.asString(), SearchDesignationResponse.class);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(searchEmployeeTypeResponse.getDesignation().length, 3);

        new APILogger().log("Search Designation Test is Completed--");
    }

}
