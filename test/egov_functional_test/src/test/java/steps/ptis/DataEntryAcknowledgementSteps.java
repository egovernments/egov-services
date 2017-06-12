package steps.ptis;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java8.En;
import pages.ptis.DataEntryAcknowledgementPage;
import steps.BaseSteps;

public class DataEntryAcknowledgementSteps extends BaseSteps implements En {

    @Then("^dataEntry Details saved successfully$")
    public void dataentryDetailsSavedSuccessfully() throws Throwable {
        String acknowledgementMessage = pageStore.get(DataEntryAcknowledgementPage.class).getdataentryAcknowledgementMessage();
        String dataentryassessmentNumber = pageStore.get(DataEntryAcknowledgementPage.class).getAssessmentNumber();
        scenarioContext.setDataScreenAssessmentNumber(dataentryassessmentNumber);
        String assessmentNumber = pageStore.get(DataEntryAcknowledgementPage.class).getAssessmentNumber();
        scenarioContext.setAssessmentNumber(assessmentNumber);
    }

    @And("^he choose to add edit DCB$")
    public void heChooseToAddEditDCB() throws Throwable {
        pageStore.get(DataEntryAcknowledgementPage.class).geteditDCB();
        pageStore.get(DataEntryAcknowledgementPage.class).enterAddDemandDetails();
    }

    @And("^he choose to close the dataentry acknowledgement screen$")
    public void heChooseToCloseTheDataentryAcknowledgementScreen() throws Throwable {
        pageStore.get(DataEntryAcknowledgementPage.class).close();
    }
}
