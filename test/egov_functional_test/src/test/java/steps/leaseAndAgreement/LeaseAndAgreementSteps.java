package steps.leaseAndAgreement;

import cucumber.api.PendingException;
import cucumber.api.java8.En;
import entities.ApprovalDetails;
import entities.leaseAndAgreement.LandAgreementDetails;
import entities.leaseAndAgreement.LandAllotteeDetails;
import excelDataFiles.ExcelReader;
import excelDataFiles.LeaseAndAgreementDataReader;
import pages.ApprovalDetailsPage;
import pages.leaseAndAgreement.LeaseAndAgreementPage;
import steps.BaseSteps;

public class LeaseAndAgreementSteps extends BaseSteps implements En {

    public LeaseAndAgreementSteps() {

        And("^user will select the required asset service application to create the agreement based on (\\w+) with action as \"([^\"]*)\"$",
                (String categoryType, String action) -> {

                    if (categoryType.contains("_"))
                        categoryType = categoryType.replaceAll("_", " ");
                    pageStore.get(LeaseAndAgreementPage.class).searchAssetApplication(scenarioContext.getApplicationNumber(), categoryType, action);
                });

        And("^user will enter the allottee details as (\\w+) and agreement details as (\\w+)$", (String allotteeDataId,
                                                                                                 String agreementDataId) -> {
            LandAllotteeDetails landAllotteeDetails = new LeaseAndAgreementDataReader(lamsTestDataFileName).
                    getAllotteeDetails(allotteeDataId);
            LandAgreementDetails landAgreementDetails = new LeaseAndAgreementDataReader(lamsTestDataFileName).
                    getAgreementDetails(agreementDataId);

            pageStore.get(LeaseAndAgreementPage.class).enterAgreementDetails(landAllotteeDetails, landAgreementDetails);
        });

        And("^user will enter the approval details of (\\w+)$", (String officer) -> {
            // Enter Approval Details
            ApprovalDetails approvalDetails = new ExcelReader(approvalDetailsTestDataFileName).getApprovalDetails(officer);
            pageStore.get(ApprovalDetailsPage.class).enterApproverDetails(approvalDetails);

            pageStore.get(LeaseAndAgreementPage.class).clickOnForwardAndCloseSuccessPage();
        });

        And("^user will collect the fee and save the agreement application number$", () -> {
            String applicationNUmber = pageStore.get(LeaseAndAgreementPage.class).collectFeeFromApplicant();
            scenarioContext.setApplicationNumber(applicationNUmber);
            pageStore.get(LeaseAndAgreementPage.class).closeAcknowledgementForm();
        });

        And("^user will approve the agreement application$", () -> {
            pageStore.get(LeaseAndAgreementPage.class).acceptApplication();
        });
    }
}
