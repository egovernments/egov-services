package steps.employeeManagement;

import cucumber.api.java8.En;
import pages.employeeManagement.calender.CalenderPage;
import steps.BaseSteps;

public class CalenderSteps extends BaseSteps implements En {

    public CalenderSteps() {

        And("^user will enter the calender details for creation$", () -> {
            pageStore.get(CalenderPage.class).enterCalenderDetails();
        });
    }
}
