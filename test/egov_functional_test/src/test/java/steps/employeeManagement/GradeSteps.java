package steps.employeeManagement;

import cucumber.api.java8.En;
import pages.employeeManagement.grade.GradePage;
import steps.BaseSteps;

public class GradeSteps extends BaseSteps implements En {

    public GradeSteps() {

        And("^user will enter the grade details for creation$", () -> {
            pageStore.get(GradePage.class).enterGradeDetails();
        });
    }
}
