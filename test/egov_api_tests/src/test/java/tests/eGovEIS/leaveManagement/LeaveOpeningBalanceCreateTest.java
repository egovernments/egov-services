package tests.eGovEIS.leaveManagement;

import builders.eGovEIS.employee.SearchEmployeeRequestBuilder;
import builders.eGovEIS.employeeMaster.leaveType.create.LeaveTypeCreateRequestBuilder;
import builders.eGovEIS.employeeMaster.leaveType.search.LeaveTypeSearchRequestBuilder;
import builders.eGovEIS.leaveManagement.create.LeaveOpeningBalanceBuilder;
import builders.eGovEIS.leaveManagement.create.LeaveTypeBuilder;
import builders.eGovEIS.leaveManagement.create.OpeningBalanceCreateRequestBuilder;
import builders.eGovEIS.leaveManagement.create.RequestInfoBuilder;
import builders.eGovEIS.leaveManagement.search.SearchOpeningBalanceRequestBuilder;
import com.jayway.restassured.response.Response;
import entities.requests.eGovEIS.employee.SearchEmployeeRequest;
import entities.requests.eGovEIS.employeeMaster.leaveType.create.LeaveTypeCreateRequest;
import entities.requests.eGovEIS.employeeMaster.leaveType.search.LeaveTypeSearchRequest;
import entities.requests.eGovEIS.leaveManagement.create.RequestInfo;
import entities.requests.eGovEIS.leaveManagement.create.LeaveOpeningBalance;
import entities.requests.eGovEIS.leaveManagement.create.LeaveType;
import entities.requests.eGovEIS.leaveManagement.create.OpeningBalanceCreateRequest;
import entities.requests.eGovEIS.leaveManagement.search.SearchOpeningBalanceRequest;
import entities.responses.eGovEIS.employeeMaster.create.LeaveTypeResponse;
import entities.responses.eGovEIS.leaveManagement.search.LeaveOpeningBalanceSearchResponse;
import entities.responses.eGovEIS.searchEmployee.SearchEmployeeResponse;
import org.junit.Assert;
import org.testng.annotations.Test;
import resources.EgovEISResource;
import resources.employeeMaster.LeaveTypeResource;
import tests.BaseAPITest;
import utils.APILogger;
import utils.Categories;
import utils.RequestHelper;
import utils.ResponseHelper;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class LeaveOpeningBalanceCreateTest extends BaseAPITest {

    @Test(groups = {Categories.HR, Categories.SANITY})
    public void leaveOpeningBalanceCreateTest() throws IOException {

        // Login Test
//        LoginResponse loginResponse = LoginAndLogoutHelper.login(narasappa);

        /*
            Collecting all Employee id's
            Here Employee Search API will be called
        */
        int[] employeeIdList = collectingAllEmployees();

        /*
            Find the leave type name based on the following format " LeaveType presentDay:presentMonth:presentYear "
            Here Leave Type Search API will be called
        */
        int leaveTypeId = findLeaveTypeNameIsPresentInDataFormatOrNot("Leave Type - " + getCurrentDate());

        /*
            Here we are checking the whether leave type consists any id or not , if the leave type is 0 we are creating the new
            leave type id with name based on " LeaveType presentDay:presentMonth:presentYear "
        */
        if (leaveTypeId == 0) {
            leaveTypeId = createTheLeaveTypeWithNameBasedOnDataFormat("Leave Type - " + getCurrentDate());
        }

        /*
            Now we need to check which employee is not having the opening balance with above Leave Type Id
        */
        new APILogger().log("Finding the employee who doesn't contain the Leave Opening Balance Test is started" +
                " with Leave Type Id as--" + leaveTypeId);

        int employeeId = 0, size = employeeIdList.length, employeeOpeningBalance;
        for (int i = 0; i < size; i++) {
            employeeOpeningBalance = findEmployeeHasOpeningBalanceWithAboveLeaveType(employeeIdList[i], leaveTypeId);
            if (employeeOpeningBalance == 0) {
                employeeId = employeeIdList[i];
                break;
            }
        }

        new APILogger().log("Finding the employee who doesn't contain the Leave Opening Balance Test is Completed" +
                " with Leave Type Id as--" + leaveTypeId);

        /*
            When employeeId == 0 that means for every employee there is a opening balance with the above leave type
            So now we need to create the new leave type in order to create the opening balance
        */
        if (employeeId == 0) {
            leaveTypeId = createTheLeaveTypeWithNameBasedOnDataFormat("Leave Type - " + get3DigitRandomInt());
            employeeId = employeeIdList[new Random().nextInt(employeeIdList.length - 0) + 0];
        }

        // Create attendances Test
        openingBalanceCreateTestMethod(employeeId, leaveTypeId);
    }

    private int[] collectingAllEmployees() throws IOException {
        new APILogger().log("Search Employee Test is Started --");
        entities.requests.eGovEIS.employee.RequestInfo requestInfo = new builders.eGovEIS.employee.RequestInfoBuilder().build();

        SearchEmployeeRequest request = new SearchEmployeeRequestBuilder().withRequestInfo(requestInfo).build();

        Response response = new EgovEISResource().searchEmployee(RequestHelper.getJsonString(request));

        org.testng.Assert.assertEquals(response.getStatusCode(), 200);

        SearchEmployeeResponse searchEmployeeResponse = (SearchEmployeeResponse)
                ResponseHelper.getResponseAsObject(response.asString(), SearchEmployeeResponse.class);


        int[] employeeList = new int[searchEmployeeResponse.getEmployee().length];
        for (int i = 0; i < searchEmployeeResponse.getEmployee().length; i++) {
            employeeList[i] = searchEmployeeResponse.getEmployee()[i].getId();
        }

        new APILogger().log("Search Employee Test is Completed --");
        return employeeList;
    }

    private int findLeaveTypeNameIsPresentInDataFormatOrNot(String leaveTypeNameInDataFormat) throws IOException {
        new APILogger().log("Finding Leave Type Name is present in Current Date Format Test is Started --");
        entities.requests.eGovEIS.employeeMaster.RequestInfo requestInfo = new builders.eGovEIS.employeeMaster.RequestInfoBuilder().build();

        LeaveTypeSearchRequest leaveTypeSearchRequest = new LeaveTypeSearchRequestBuilder()
                .withRequestInfo(requestInfo)
                .build();

        Response response = new LeaveTypeResource().search(RequestHelper.getJsonString(leaveTypeSearchRequest));

        LeaveTypeResponse leaveTypeSearchResponse = (LeaveTypeResponse)
                ResponseHelper.getResponseAsObject(response.asString(), LeaveTypeResponse.class);

        Assert.assertEquals(response.getStatusCode(), 200);

        int leaveTypeId = 0;
        for (int i = 0; i < leaveTypeSearchResponse.getLeaveType().length; i++) {
            if (leaveTypeNameInDataFormat.equalsIgnoreCase(leaveTypeSearchResponse.getLeaveType()[i].getName())) {
                leaveTypeId = leaveTypeSearchResponse.getLeaveType()[i].getId();
            }
        }
        new APILogger().log("Finding Leave Type Name in Current Date Format Test is Completed --");
        return leaveTypeId;
    }

    private int createTheLeaveTypeWithNameBasedOnDataFormat(String leaveNameFormat) throws IOException {

        new APILogger().log("Creating a Leave Type With Current Data Format Test is Started --");

        entities.requests.eGovEIS.employeeMaster.RequestInfo requestInfo = new builders.eGovEIS.employeeMaster.RequestInfoBuilder().build();

        entities.requests.eGovEIS.employeeMaster.leaveType.create.LeaveType leaveType1 = new builders.eGovEIS.employeeMaster.leaveType.create.LeaveTypeBuilder()
                .withName(leaveNameFormat)
                .withDescription("Leave Type")
                .withHalfdayAllowed(true)
                .withPayEligible(true)
                .withAccumulative(true)
                .withEncashable(false)
                .withActive(true)
                .withCreatedBy("")
                .withCreatedDate("")
                .withLastModifiedBy("")
                .withLastModifiedDate("")
                .withTenantId("ap.kurnool")
                .build();

        entities.requests.eGovEIS.employeeMaster.leaveType.create.LeaveType[] leaveTypes = new entities.requests.eGovEIS.employeeMaster.leaveType.create.LeaveType[1];
        leaveTypes[0] = leaveType1;

        LeaveTypeCreateRequest leaveTypeCreateRequest = new LeaveTypeCreateRequestBuilder()
                .withRequestInfo(requestInfo)
                .withLeaveType(leaveTypes)
                .build();

        Response response = new LeaveTypeResource().create(RequestHelper.getJsonString(leaveTypeCreateRequest));

        LeaveTypeResponse leaveTypeCreateResponse = (LeaveTypeResponse)
                ResponseHelper.getResponseAsObject(response.asString(), LeaveTypeResponse.class);

        org.testng.Assert.assertEquals(response.getStatusCode(), 200);
        new APILogger().log("Leave Type Create Test is Completed --");

        // Search the Leave Type based name and find the id
        entities.requests.eGovEIS.employeeMaster.RequestInfo requestInfo1 = new builders.eGovEIS.employeeMaster.RequestInfoBuilder().build();

        LeaveTypeSearchRequest leaveTypeSearchRequest = new LeaveTypeSearchRequestBuilder()
                .withRequestInfo(requestInfo1)
                .build();

        Response response1 = new LeaveTypeResource().search(RequestHelper.getJsonString(leaveTypeSearchRequest));

        LeaveTypeResponse leaveTypeSearchResponse = (LeaveTypeResponse)
                ResponseHelper.getResponseAsObject(response1.asString(), LeaveTypeResponse.class);

        org.testng.Assert.assertEquals(response1.getStatusCode(), 200);

        int leaveId = 0;
        for (int i = 0; i < leaveTypeSearchResponse.getLeaveType().length; i++) {
            if (leaveTypeSearchResponse.getLeaveType()[i].getName().contains(leaveNameFormat)) {
                leaveId = leaveTypeSearchResponse.getLeaveType()[i].getId();
            }
        }
        new APILogger().log("Creating a Leave Type With Current Data Format Test is Completed --");
        return leaveId;
    }

    private int findEmployeeHasOpeningBalanceWithAboveLeaveType(int employeeId, int leaveTypeId) throws IOException {
        entities.requests.eGovEIS.leaveManagement.search.RequestInfo requestInfo = new builders.eGovEIS.leaveManagement.search.RequestInfoBuilder()
                .withAuthToken("2708de2c-8eb1-4871-9237-8f301f808b37")
                .withTs("01-01-2017 01:01:01")
                .build();

        SearchOpeningBalanceRequest searchOpeningBalanceRequest = new SearchOpeningBalanceRequestBuilder()
                .withRequestInfo(requestInfo)
                .build();

        String path = "&employee=" + employeeId + "&leaveType=" + leaveTypeId + "&year=" + getCurrentDate().split("/")[2];

        Response response = new EgovEISResource().hrLeaveSearchOpeningBalance(RequestHelper.getJsonString(searchOpeningBalanceRequest), path);

        LeaveOpeningBalanceSearchResponse leaveOpeningBalanceSearchResponse = (LeaveOpeningBalanceSearchResponse)
                ResponseHelper.getResponseAsObject(response.asString(), LeaveOpeningBalanceSearchResponse.class);
        Assert.assertEquals(response.getStatusCode(), 200);
        return leaveOpeningBalanceSearchResponse.getLeaveOpeningBalance().length;
    }

    private void openingBalanceCreateTestMethod(int employeeId, int leaveTypeId) throws IOException {

        LeaveType leaveType = new LeaveTypeBuilder()
                .withId(leaveTypeId)
                .build();

        LeaveOpeningBalance leaveOpeningBalance = new LeaveOpeningBalanceBuilder()
                .withLeaveType(leaveType)
                .withEmployee(employeeId)
                .withNoOfDays(Integer.parseInt(get3DigitRandomInt()))
                .withTenantId("ap.kurnool")
                .build();

//        int noOfDays = leaveOpeningBalance.getNoOfDays();

        RequestInfo requestInfo = new RequestInfoBuilder()
                .withAuthToken("2708de2c-8eb1-4871-9237-8f301f808b37")
                .withTs("01-01-2017 01:01:01")
                .build();

        OpeningBalanceCreateRequest openingBalanceCreateRequest = new OpeningBalanceCreateRequestBuilder(leaveOpeningBalance)
                .withRequestInfo(requestInfo)
                .build();

        Response response = new EgovEISResource().hrLeaveCreateOpeningBalance(RequestHelper.getJsonString(openingBalanceCreateRequest));
        Assert.assertEquals(response.getStatusCode(), 200);

        searchOpeningBalanceTestMethod(employeeId, leaveTypeId, openingBalanceCreateRequest);
    }

    private void searchOpeningBalanceTestMethod(int employeeId, int leaveTypeId, OpeningBalanceCreateRequest openingBalanceCreateRequest) throws IOException {

        entities.requests.eGovEIS.leaveManagement.search.RequestInfo requestInfo = new builders.eGovEIS.leaveManagement.search.RequestInfoBuilder()
                .withAuthToken("2708de2c-8eb1-4871-9237-8f301f808b37")
                .withTs("01-01-2017 01:01:01")
                .build();

        SearchOpeningBalanceRequest searchOpeningBalanceRequest = new SearchOpeningBalanceRequestBuilder()
                .withRequestInfo(requestInfo)
                .build();

        String path = "&employee=" + employeeId + "&leaveType=" + leaveTypeId + "&year=" + getCurrentDate().split("/")[2];
        new APILogger().log("Search HR Leave Opening Balance Test is started with-- " + RequestHelper.getJsonString(searchOpeningBalanceRequest));
        Response response = new EgovEISResource().hrLeaveSearchOpeningBalance(RequestHelper.getJsonString(searchOpeningBalanceRequest), path);
        new APILogger().log("Search HR Leave Opening Balance Test is completed with-- " + response.asString());

        LeaveOpeningBalanceSearchResponse leaveOpeningBalanceSearchResponse = (LeaveOpeningBalanceSearchResponse)
                ResponseHelper.getResponseAsObject(response.asString(), LeaveOpeningBalanceSearchResponse.class);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(leaveOpeningBalanceSearchResponse.getLeaveOpeningBalance().length == 1);

        // Updating the created Opening Balance
        updateSearchOpeningBalanceTestMethod(leaveOpeningBalanceSearchResponse.getLeaveOpeningBalance()[0].getId(), openingBalanceCreateRequest);
    }

    private void updateSearchOpeningBalanceTestMethod(int id, OpeningBalanceCreateRequest openingBalanceCreateRequest) throws IOException {

        LeaveType leaveType = new LeaveTypeBuilder()
                .withId(openingBalanceCreateRequest.getLeaveOpeningBalance()[0].getLeaveType().getId())
                .build();

        LeaveOpeningBalance leaveOpeningBalance = new LeaveOpeningBalanceBuilder()
                .withLeaveType(leaveType)
                .withEmployee(openingBalanceCreateRequest.getLeaveOpeningBalance()[0].getEmployee())
                .withNoOfDays(openingBalanceCreateRequest.getLeaveOpeningBalance()[0].getNoOfDays() + 1)
                .withTenantId(openingBalanceCreateRequest.getLeaveOpeningBalance()[0].getTenantId())
                .build();

        RequestInfo requestInfo = new RequestInfoBuilder()
                .withAuthToken(openingBalanceCreateRequest.getRequestInfo().getAuthToken())
                .withTs(openingBalanceCreateRequest.getRequestInfo().getTs())
                .build();

        OpeningBalanceCreateRequest openingBalanceUpdateRequest = new OpeningBalanceCreateRequestBuilder(leaveOpeningBalance)
                .withRequestInfo(requestInfo)
                .build();

        Response response = new EgovEISResource().hrLeaveUpdateOpeningBalance(RequestHelper.getJsonString(openingBalanceUpdateRequest), id);

        LeaveOpeningBalanceSearchResponse leaveOpeningBalanceUpdateResponse = (LeaveOpeningBalanceSearchResponse)
                ResponseHelper.getResponseAsObject(response.asString(), LeaveOpeningBalanceSearchResponse.class);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(leaveOpeningBalanceUpdateResponse.getLeaveOpeningBalance()[0].getNoOfDays(),openingBalanceCreateRequest.getLeaveOpeningBalance()[0].getNoOfDays() + 1);
    }
}
