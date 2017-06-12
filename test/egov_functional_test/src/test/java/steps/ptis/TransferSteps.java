package steps.ptis;

import cucumber.api.PendingException;
import cucumber.api.java8.En;
import entities.ApprovalDetails;
import entities.ptis.RegistrationDetails;
import excelDataFiles.ExcelReader;
import excelDataFiles.PTISDataReader;
import pages.ApprovalDetailsPage;
import pages.ptis.PropertyAcknowledgementPage;
import pages.ptis.PropertyDetailsPage;
import pages.ptis.TransferDetailsPage;
import steps.BaseSteps;

public class TransferSteps extends BaseSteps implements En {
    public TransferSteps() {
        And("^he pay tax using Cash$", () -> {
            pageStore.get(PropertyDetailsPage.class).payCash();
            pageStore.get(PropertyAcknowledgementPage.class).close1();
        });
        And("^he chooses Registration already done button$", () -> {
            pageStore.get(TransferDetailsPage.class).chooseRegistrationAlreadyDone();
        });
        And("^he enters registration details for the property (\\w+)$", (String registrationDetailsDataId) -> {
            RegistrationDetails registrationDetails = new PTISDataReader(ptisTestDataFileName).getRegistrationDetails(registrationDetailsDataId);
            pageStore.get(TransferDetailsPage.class).enterRegistrationDetails(registrationDetails);
        });
        And("^he enters enclosure details$", () -> {
            pageStore.get(TransferDetailsPage.class).enterEnclosureDetails();
        });
        And("^he searches for the assessment with mutation assessment number$", () -> {
            pageStore.get(TransferDetailsPage.class).searchAssessmentNumber(scenarioContext.getAssessmentNumber());
        });
        And("^he generate title transfer notice$", () -> {
            pageStore.get(TransferDetailsPage.class).generateTitleTransferNotice();
        });
        And("^he selects the exemption reason from drop down$", () -> {
            pageStore.get(TransferDetailsPage.class).selectExemptionReason();
            pageStore.get(TransferDetailsPage.class).uploadFiles();
        });
        And("^he forwarding for approval to (.*)$", (String approvalDetailsDataId) -> {
            ApprovalDetails approvalDetails = new ExcelReader(approvalDetailsTestDataFileName).getApprovalDetails(approvalDetailsDataId);
            pageStore.get(ApprovalDetailsPage.class).enterApprovalDetails(approvalDetails);
            pageStore.get(TransferDetailsPage.class).selectDeclarationCheckBox();
            pageStore.get(ApprovalDetailsPage.class).forward();
        });
        And("^he will copy the acknowledgement message with application number (.*)$", (String type) -> {
            String msg = pageStore.get(TransferDetailsPage.class).getApplicationNumber(type);
            scenarioContext.setApplicationNumber(scenarioContext.getDataScreenAssessmentNumber());
            scenarioContext.setActualMessage(msg);
            pageStore.get(TransferDetailsPage.class).close();
        });
        And("^current user closes tax exemption acknowledgement$", () -> {
            pageStore.get(TransferDetailsPage.class).closesAcknowledgement();
        });
        And("^he clicks on permises button$", () -> {
            pageStore.get(TransferDetailsPage.class).clickOnPremisesCheckBox();
        });

    }
}
