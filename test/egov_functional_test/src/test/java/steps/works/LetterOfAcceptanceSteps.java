package steps.works;

import cucumber.api.java8.En;
import pages.works.LetterOfAcceptancePage;
import steps.BaseSteps;

public class LetterOfAcceptanceSteps extends BaseSteps implements En {
    public LetterOfAcceptanceSteps() {

        And("^he enters the mandatory details$", () -> {
            pageStore.get(LetterOfAcceptancePage.class).enterLOAdetails();
        });
        Then("^he save the file and view the LOA pdf$", () -> {
            String number = pageStore.get(LetterOfAcceptancePage.class).saveAndClose();
            scenarioContext.setApplicationNumber(number);

            String actualMsg = pageStore.get(LetterOfAcceptancePage.class).successMessage();
            scenarioContext.setActualMessage(actualMsg);

            pageStore.get(LetterOfAcceptancePage.class).close();
        });

        And("^he search for LOA$", () -> {
            pageStore.get(LetterOfAcceptancePage.class).searchForLOA(scenarioContext.getApplicationNumber());
        });

        And("^he search for LOA for modify$", () -> {
            pageStore.get(LetterOfAcceptancePage.class).searchForLOAModify(scenarioContext.getApplicationNumber());

            String actualMsg = pageStore.get(LetterOfAcceptancePage.class).successMessage();
            scenarioContext.setActualMessage(actualMsg);

            pageStore.get(LetterOfAcceptancePage.class).close();
        });
        And("^he select the required application$", () -> {
            pageStore.get(LetterOfAcceptancePage.class).searchForApplication();
        });
        And("^he select the required spillover estimate from search results$", () -> {
            pageStore.get(LetterOfAcceptancePage.class).searchForSpilloverEstimate();
        });
        And("^he enters the mandatory details for creating LOA$", () -> {
            pageStore.get(LetterOfAcceptancePage.class).entersSpilloverLOADetails();
        });
    }
}
