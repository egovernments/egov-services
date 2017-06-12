package tests.commonMasters.holiday;

import builders.commonMaster.createHoliday.CalendarYearBuilder;
import builders.commonMaster.createHoliday.CreateHolidayRequestBuilder;
import builders.commonMaster.createHoliday.HolidayBuilder;
import builders.commonMaster.createHoliday.RequestInfoBuilder;
import com.jayway.restassured.response.Response;
import entities.requests.commonMasters.createHoliday.CalendarYear;
import entities.requests.commonMasters.createHoliday.CreateHolidayRequest;
import entities.requests.commonMasters.createHoliday.Holiday;
import entities.requests.commonMasters.createHoliday.RequestInfo;
import entities.responses.login.LoginResponse;
import org.junit.Assert;
import org.testng.annotations.Test;
import resources.CommonMasterResource;
import tests.BaseAPITest;
import utils.APILogger;
import utils.Categories;
import utils.LoginAndLogoutHelper;
import utils.RequestHelper;

import java.io.IOException;

import static data.UserData.ADMIN;
import static data.UserData.NARASAPPA;

public class CreateHolidayTest extends BaseAPITest {

    @Test(groups = {Categories.HR, Categories.SANITY})
    public void createHolidayTest() throws IOException {

        // Login Test
        String sessionId = LoginAndLogoutHelper.loginFromPilotService(NARASAPPA);
        System.out.println(sessionId);

        // Create Holiday Test
        createHolidayTestMethod(sessionId);

    }

    private void createHolidayTestMethod(String sessionId) {

        RequestInfo requestInfo = new RequestInfoBuilder().build();

        CalendarYear calendarYear = new CalendarYearBuilder()
                .build();

        Holiday holiday = new HolidayBuilder()
                .withCalendarYear(calendarYear)
                .withId(30)
                .withName("Good Friday")
                .withApplicableOn("14/04/2017")
                .build();

        CreateHolidayRequest createHolidayRequest = new CreateHolidayRequestBuilder()
                .withHoliday(holiday)
                .withRequestInfo(requestInfo)
                .build();

        Response response = new CommonMasterResource()
                .createHoliday(RequestHelper.getJsonString(createHolidayRequest), sessionId);

        Assert.assertEquals(response.getStatusCode(), 200);
        new APILogger().log("Create Holiday Test is Completed --");

        // Logout Test
        pilotLogoutService(sessionId);
    }

}
