package steps.lcms;

import cucumber.api.java8.En;
import entities.lcms.CreateLegalCase;
import excelDataFiles.LCMSDataReader;
import pages.lcms.LegalCaseManagementPage;
import steps.BaseSteps;

public class LegalCaseManagementSteps extends BaseSteps implements En {

    public LegalCaseManagementSteps() {

        And("^user will enter the legal case details as (\\w+)$", (String legalCaseData) -> {

            CreateLegalCase createLegalCase = new LCMSDataReader(legalCaseTestDataFileName).getCreateLegalCaseDetails(legalCaseData);
            pageStore.get(LegalCaseManagementPage.class).enterLegalCaseDetails(createLegalCase);
        });

        And("^user closes the successful acknowledgement form$", () -> {
            String messageAndFileNumber = pageStore.get(LegalCaseManagementPage.class).closesAcknowledgementForm();
            scenarioContext.setActualMessage(messageAndFileNumber.split("\\>")[0]);
            //Here case file number acts as application number
            scenarioContext.setApplicationNumber(messageAndFileNumber.split("\\>")[1]);
        });

        And("^user will enter the case file number to search the file$", () -> {
            //Here case file number acts as application number
            pageStore.get(LegalCaseManagementPage.class).searchCaseFile(scenarioContext.getApplicationNumber());
        });

        And("^user will take the corresponding action on above as (\\w+)$", (String action) -> {
            pageStore.get(LegalCaseManagementPage.class).clickOnCorrespondingAction(action);
        });

        And("^user will closes the successful created or updated page$", () -> {
            String message = pageStore.get(LegalCaseManagementPage.class).closeCreatedOrUpdatedPage();
            scenarioContext.setActualMessage(message);
        });

        And("^user will enter the details of judgment implementation details based on (\\w+)$", (String mode) -> {
            pageStore.get(LegalCaseManagementPage.class).enterJudgmentImplementationDetails(mode);
        });
    }
}
