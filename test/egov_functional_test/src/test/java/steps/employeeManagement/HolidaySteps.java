package steps.employeeManagement;

import cucumber.api.java8.En;
import pages.employeeManagement.holiday.HolidayPage;
import steps.BaseSteps;

public class HolidaySteps extends BaseSteps implements En {

    public HolidaySteps() {

        And("^user will enter the holiday details for creation$", () -> {
            pageStore.get(HolidayPage.class).enterHolidayDetails();
        });
    }
}
