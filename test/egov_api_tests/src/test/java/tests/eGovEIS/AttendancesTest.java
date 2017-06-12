package tests.eGovEIS;

import builders.eGovEIS.attendances.AttendanceBuilder;
import builders.eGovEIS.attendances.CreateAttendanceRequestBuilder;
import builders.eGovEIS.attendances.RequestInfoBuilder;
import builders.eGovEIS.attendances.SearchAttendanceRequestBuilder;
import com.jayway.restassured.response.Response;
import entities.requests.eGovEIS.attendances.Attendance;
import entities.requests.eGovEIS.attendances.CreateAttendanceRequest;
import entities.requests.eGovEIS.attendances.RequestInfo;
import entities.requests.eGovEIS.attendances.SearchAttendanceRequest;
import entities.responses.eGovEIS.SearchAttendanceResponse;
import entities.responses.eGovEIS.createAttendance.CreateAttendanceResponse;
import entities.responses.login.LoginResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import resources.EgovEISResource;
import tests.BaseAPITest;
import utils.*;

import java.io.IOException;

import static data.UserData.NARASAPPA;

public class AttendancesTest extends BaseAPITest {

    @Test(groups = {Categories.HR, Categories.SANITY})
    public void CreateAttendancesTest() throws IOException {

        // Login Test
        LoginResponse loginResponse = LoginAndLogoutHelper.login(NARASAPPA);

        // Create attendances Test
        createAttendancesTestMethod(loginResponse);
    }

    @Test(groups = {Categories.HR, Categories.SANITY})
    public void SearchAttendancesTest() throws IOException {

        // Login Test
        LoginResponse loginResponse = LoginAndLogoutHelper.login(NARASAPPA);

        // Search attendances Test
        searchAttendancesTestMethod(loginResponse);
    }

    public void createAttendancesTestMethod(LoginResponse loginResponse) throws IOException {
        RequestInfo requestInfo = new RequestInfoBuilder().withAuthToken(loginResponse.getAccess_token()).build();
        Attendance attendance = new AttendanceBuilder().withAttendanceDate(getRandomDate()).build();
        CreateAttendanceRequest request = new CreateAttendanceRequestBuilder().withRequestInfo(requestInfo).withAttendance(attendance).build();

        String jsonData = RequestHelper.getJsonString(request);
        Response response = new EgovEISResource().createAttendance(jsonData, loginResponse.getAccess_token());
        CreateAttendanceResponse createAttendanceResponse = (CreateAttendanceResponse)
                ResponseHelper.getResponseAsObject(response.asString(), CreateAttendanceResponse.class);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(createAttendanceResponse.getResponseInfo().getStatus(), request.getRequestInfo().getStatus());

        new APILogger().log("Create Attendance Test is completed --");
    }

    public void searchAttendancesTestMethod(LoginResponse loginResponse) throws IOException {
        entities.requests.eGovEIS.RequestInfo requestInfo = new RequestInfoBuilder("Search").withAuthToken1(loginResponse.getAccess_token()).build1();
        SearchAttendanceRequest request = new SearchAttendanceRequestBuilder().withRequestInfo(requestInfo).build();

        String jsonData = RequestHelper.getJsonString(request);
        Response response = new EgovEISResource().searchAttendance(jsonData, loginResponse.getAccess_token());
        SearchAttendanceResponse searchAttendanceResponse = (SearchAttendanceResponse) ResponseHelper.getResponseAsObject(response.asString(), SearchAttendanceResponse.class);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(searchAttendanceResponse.getAttendance()[0].getEmployee(), "1");
        Assert.assertEquals(searchAttendanceResponse.getAttendance()[0].getType().getCode(), "P", "Assert attendance type code");
        Assert.assertEquals(searchAttendanceResponse.getAttendance()[0].getAttendanceDate(), "2010-03-31", "Assert on Attendance Date");

        Assert.assertEquals(searchAttendanceResponse.getAttendance()[1].getEmployee(), "1");
        Assert.assertEquals(searchAttendanceResponse.getAttendance()[1].getAttendanceDate(), "2010-05-24");
        Assert.assertEquals(searchAttendanceResponse.getAttendance()[1].getType().getCode(), "P");

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(searchAttendanceResponse.getAttendance()[0].getEmployee(), "1");
        softAssert.assertEquals(searchAttendanceResponse.getAttendance()[1].getEmployee(), "1");

        try {
            softAssert.assertAll();
        } catch (AssertionError error) {
            System.out.println("Assertion Failed because of: " + error.getMessage());
        }

        new APILogger().log("Search attendances Test is completed --");
    }
}