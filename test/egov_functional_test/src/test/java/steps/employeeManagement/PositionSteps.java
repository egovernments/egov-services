package steps.employeeManagement;

import cucumber.api.java8.En;
import pages.employeeManagement.position.PositionPage;
import steps.BaseSteps;

public class PositionSteps extends BaseSteps implements En {

    public PositionSteps() {
        And("^user will enter the position details for creation$", () -> {
            pageStore.get(PositionPage.class).enterPositionDetails();
        });
    }
}
