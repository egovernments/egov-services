package steps.ptis;

import cucumber.api.PendingException;
import cucumber.api.java8.En;
import pages.ptis.PropertyAcknowledgementPage;
import pages.ptis.PropertyDetailsPage;
import steps.BaseSteps;

public class PropertyAcknowledgementSteps extends BaseSteps implements En {

    public PropertyAcknowledgementSteps() {

        Then("^create property details get saved successfully by generating assesssment number$", () -> {
            String assessmentNumber = pageStore.get(PropertyAcknowledgementPage.class).getAssessmentNumber();
            scenarioContext.setAssessmentNumber(assessmentNumber);
        });

        And("^he will copy the acknowledgement message with assessment number (.*)$", (String type) -> {
            String msg = pageStore.get(PropertyAcknowledgementPage.class).getAssessmentNumberNew(type);
            scenarioContext.setActualMessage(msg);
            scenarioContext.setAssessmentNumber(msg.substring(msg.lastIndexOf(" ") + 1));
            pageStore.get(PropertyAcknowledgementPage.class).close();
        });
        And("^he will copy the acknowledgement of above rejected (.*)$", (String type) -> {
            String msg = pageStore.get(PropertyAcknowledgementPage.class).getAssessmentNumberNew(type);
            scenarioContext.setActualMessage(msg);
            pageStore.get(PropertyAcknowledgementPage.class).close();
        });
        And("^current user closes acknowledgement$", () -> {
            pageStore.get(PropertyAcknowledgementPage.class).close();
        });
        When("^commissioner closes acknowledgement$", () -> {
            pageStore.get(PropertyAcknowledgementPage.class).closeFromCommisionersLogin();
        });
        And("^he cancels the print$", () -> {
            pageStore.get(PropertyAcknowledgementPage.class).cancelPrint();
        });
        And("^user will see the successfull page and view the details$", () -> {
            pageStore.get(PropertyAcknowledgementPage.class).toViewSubmissionPage();
        });
        And("^user will close the data entry page$", () -> {
            pageStore.get(PropertyAcknowledgementPage.class).toCloseDataEntryPage();
        });
        And("^user closes acknowledgement form$", () -> {
            pageStore.get(PropertyAcknowledgementPage.class).toCloseAdditionalConnectionPage();
        });
    }
}
