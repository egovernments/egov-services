package steps.employeeManagement;

import cucumber.api.java8.En;
import pages.employeeManagement.designation.DesignationPage;
import steps.BaseSteps;

public class DesignationSteps extends BaseSteps implements En {

    public DesignationSteps() {

        And("^user will enter the designation details for creation$", () -> {
            pageStore.get(DesignationPage.class).enterDesignationDetails();
        });
    }
}
