package tests;

import com.jayway.restassured.RestAssured;
import org.apache.commons.lang3.RandomUtils;
import org.testng.Reporter;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;
import utils.Categories;
import utils.LoginAndLogoutHelper;
import utils.ResourceHelper;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static java.lang.String.format;

public class BaseAPITest {

    public BaseAPITest() {
        RestAssured.baseURI = new ResourceHelper().getBaseURI();
    }

    public static int randBetween(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }

    @BeforeMethod(alwaysRun = true)
    public void testSetup(Method method) {
        Reporter.log("Test Method Name -- " + method.getName(), true);
    }

    @BeforeGroups(groups = Categories.SANITY, alwaysRun = true)
    public void setUp() throws IOException {
    }

    public String getRandomDate() {

        GregorianCalendar gc = new GregorianCalendar();

        int year = randBetween(2010, 2016);

        gc.set(gc.YEAR, year);

        int dayOfYear = randBetween(1, gc.getActualMaximum(gc.DAY_OF_YEAR));

        gc.set(gc.DAY_OF_YEAR, dayOfYear);

        String finalDate = format(gc.get(gc.DAY_OF_MONTH) + "/" + (gc.get(gc.MONTH) + 1) + "/" + gc.get(gc.YEAR));

        return finalDate;
    }

    protected String get3DigitRandomInt() {
        return String.valueOf((RandomUtils.nextInt(100, 999)));
    }

    protected String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    protected void pilotLogoutService(String sessionId){
        new LoginAndLogoutHelper().logoutFromPilotService(sessionId);

    }
}
