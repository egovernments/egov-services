package steps;

import cucumber.api.java8.En;
import pages.DashboardPage;

public class DashboardSteps extends BaseSteps implements En {
    public DashboardSteps() {

        And("^current user logs out$", () -> {
            pageStore.get(DashboardPage.class).logOut();
        });

        And("^he open application from drafts items$", () -> {
            pageStore.get(DashboardPage.class).openApplication("Property Tax");
        });

        And("^he chooses to act upon on receipt$", () -> {
            pageStore.get(DashboardPage.class).openApplication("Property Tax");
        });

        And("^officer search for the assignment mode as (\\w+)$", (String assignmentMode) -> {
            pageStore.get(DashboardPage.class).chooseForModeOFAssignment(assignmentMode);
        });

        And("^user will select the required screen as \"([^\"]*)\"$", (String screenName) -> {
            pageStore.get(DashboardPage.class).chooseScreen(screenName);
        });

        And("^user will select the required screen as \"([^\"]*)\" with condition as \"([^\"]*)\"$", (String screenName, String condition) -> {
            pageStore.get(DashboardPage.class).chooseScreen(screenName, condition);
        });

        And("^he chooses to act upon above (.*)$", (String type) -> {
            if (type.equals("application number")) {
                pageStore.get(DashboardPage.class).openApplication(scenarioContext.getApplicationNumber());
            } else if (type.equals("assessment number")) {
                pageStore.get(DashboardPage.class).openApplication(scenarioContext.getAssessmentNumber());
            }
        });
    }
}