package tests.eGovEIS.employeeMaster;

import builders.eGovEIS.employee.*;
import com.jayway.restassured.response.Response;
import entities.requests.eGovEIS.employee.*;
import entities.responses.eGovEIS.createEmployee.CreateEmployeeResponse;
import entities.responses.eGovEIS.searchEmployee.SearchEmployeeResponse;
import entities.responses.login.LoginResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import resources.EgovEISResource;
import tests.BaseAPITest;
import utils.*;

import java.io.IOException;

import static data.UserData.NARASAPPA;

public class EmployeeMasterTest extends BaseAPITest {

    @Test(groups = {Categories.HR, Categories.SANITY})
    public void EmployeeTest() throws IOException {

        //Login Test
        LoginResponse loginResponse = LoginAndLogoutHelper.login(NARASAPPA);

        //Create createEmployee Test
        CreateEmployeeResponse createEmployeeResponse = createEmployeeTestMethod(loginResponse);

        //Search createEmployee Test with Criteria Id
        searchEmployeeTestMethod(loginResponse, "id", createEmployeeResponse);

        //Search createEmployee Test with Criteria Code
        searchEmployeeTestMethod(loginResponse, "code", createEmployeeResponse);
    }

    // Create createEmployee Test
    public CreateEmployeeResponse createEmployeeTestMethod(LoginResponse loginResponse) throws IOException {
        RequestInfo requestInfo = new RequestInfoBuilder().withAuthToken(loginResponse.getAccess_token()).build();

        User user = new UserBuilder().withUserName("TestUser" + get3DigitRandomInt()).build();
        Employee employee = new EmployeeBuilder().withPassportNo("IND12" + get3DigitRandomInt()).withGpfNo("12" + get3DigitRandomInt())
                .withUser(user).withCode("EMPLOYEE" + get3DigitRandomInt()).build();

        CreateEmployeeRequest request = new CreateEmployeeRequestBuilder().withRequestInfo(requestInfo).withEmployee(employee).build();

        String jsonString = RequestHelper.getJsonString(request);

        Response response = new EgovEISResource().createEmployee(jsonString);
        Assert.assertEquals(response.getStatusCode(), 200);

        CreateEmployeeResponse createEmployeeResponse = (CreateEmployeeResponse)
                ResponseHelper.getResponseAsObject(response.asString(), CreateEmployeeResponse.class);

        Assert.assertEquals(request.getEmployee().getPassportNo(), createEmployeeResponse.getEmployee().getPassportNo());
        Assert.assertEquals(request.getEmployee().getUser().getUserName(), createEmployeeResponse.getEmployee().getUser().getUserName());
        Assert.assertEquals(request.getEmployee().getGpfNo(), createEmployeeResponse.getEmployee().getGpfNo());

        new APILogger().log("Create employee Test is Completed");

        return createEmployeeResponse;
    }

    // Search createEmployee Test
    public void searchEmployeeTestMethod(LoginResponse loginResponse, String criteria, CreateEmployeeResponse createEmployeeResponse) throws IOException {
        RequestInfo requestInfo = new RequestInfoBuilder().withAuthToken(loginResponse.getAccess_token()).build();

        SearchEmployeeRequest request = new SearchEmployeeRequestBuilder().withRequestInfo(requestInfo).build();

        String json = RequestHelper.getJsonString(request);
        String path = null;

        switch (criteria) {

            case "id":
                path = "&id=" + createEmployeeResponse.getEmployee().getId();
                break;

            case "code":
                path = "&code=" + createEmployeeResponse.getEmployee().getCode();
                break;
        }

        Response response = new EgovEISResource().searchEmployee(json, path);

        Assert.assertEquals(response.getStatusCode(), 200);

        SearchEmployeeResponse searchEmployeeResponse = (SearchEmployeeResponse)
                ResponseHelper.getResponseAsObject(response.asString(), SearchEmployeeResponse.class);

        Assert.assertEquals(createEmployeeResponse.getEmployee().getUser().getUserName(), searchEmployeeResponse.getEmployee()[0].getUserName());

        new APILogger().log("Search employee with " + criteria + " is Completed");
    }
}