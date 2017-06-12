package steps.marriageRegistration;

import cucumber.api.java8.En;
import entities.ApprovalDetails;
import entities.marriageRegistration.MarriageRegistrationInformation;
import excelDataFiles.ExcelReader;
import excelDataFiles.MarriageRegistrationDataReader;
import pages.ApprovalDetailsPage;
import pages.marriageRegistration.MarriageRegistrationPage;
import pages.sewerageTax.NewSewerageConnectionPage;
import steps.BaseSteps;

public class MarriageRegistrationSteps extends BaseSteps implements En {
    public MarriageRegistrationSteps() {

        And("^he enters the applicants details as (\\w+)$", (String generalInformationDataId) -> {
            MarriageRegistrationInformation marriageRegistrationInformation = new MarriageRegistrationDataReader(marriageRegistrationTestDataFileName).getApplicantsInformation(generalInformationDataId);
            pageStore.get(MarriageRegistrationPage.class).enterApplicantsInformation(marriageRegistrationInformation);
        });
        And("^he enters the bridegroom information as (\\w+) (\\w+)$", (String bridegroomInformationDataId, String brideInformationDataId) -> {
            MarriageRegistrationInformation marriageRegistrationInformation1 = new MarriageRegistrationDataReader(marriageRegistrationTestDataFileName).getBrideGroomInformation(bridegroomInformationDataId);
            pageStore.get(MarriageRegistrationPage.class).enterBrideGroomInformation(marriageRegistrationInformation1, "husband");

            MarriageRegistrationInformation marriageRegistrationInformation2 = new MarriageRegistrationDataReader(marriageRegistrationTestDataFileName).getBrideGroomInformation(brideInformationDataId);
            pageStore.get(MarriageRegistrationPage.class).enterBrideGroomInformation(marriageRegistrationInformation2, "wife");
        });
        And("^he enters the Witnesses Information$", () -> {
            pageStore.get(MarriageRegistrationPage.class).entersWitnessesInformation();
        });
        And("^he enters the checklist$", () -> {
            pageStore.get(MarriageRegistrationPage.class).enterChecklist();
        });
        And("^he forward to commissioner and closes the acknowledgement$", () -> {
            String approverDetailsDataId = "commissioner1";

            ApprovalDetails approvalDetails = new ExcelReader(approvalDetailsTestDataFileName).getApprovalDetails(approverDetailsDataId);
            pageStore.get(ApprovalDetailsPage.class).enterApprovalDetails(approvalDetails);

            pageStore.get(NewSewerageConnectionPage.class).forward();

            scenarioContext.setApplicationNumber(pageStore.get(MarriageRegistrationPage.class).getApplicationNumber());

            scenarioContext.setActualMessage(pageStore.get(MarriageRegistrationPage.class).getSuccessMessage());

            pageStore.get(MarriageRegistrationPage.class).closeApplication();
        });
        And("^he approve the new marriage application  and close the acknowledgement$", () -> {
//            pageStore.get(MarriageRegistrationPage.class).enterMarriageRegNum();
            pageStore.get(MarriageRegistrationPage.class).approve();
            scenarioContext.setReferenceNumber(pageStore.get(MarriageRegistrationPage.class).getRegistrationNumber());
            scenarioContext.setActualMessage(pageStore.get(MarriageRegistrationPage.class).getSuccessMessage());
            pageStore.get(MarriageRegistrationPage.class).closeApplication();
        });
        And("^he enters the serial and page number$", () -> {
            pageStore.get(MarriageRegistrationPage.class).enterMarriageRegNum();
        });

        And("^he search for above application number to collect marriage Registration fee$", () -> {
            pageStore.get(MarriageRegistrationPage.class).searchForMarriageApplicationNumberToCollect(scenarioContext.getApplicationNumber(), "registration");
            pageStore.get(MarriageRegistrationPage.class).clickOnCollectDropdown();
        });
        And("^he submit the data entry$", () -> {
            String message = pageStore.get(MarriageRegistrationPage.class).isSuccesful();
            String number = message.split("\\s")[7];
            scenarioContext.setActualMessage(message);
            scenarioContext.setApplicationNumber(number);
        });
        And("^he search the marrige application$", () -> {
            pageStore.get(MarriageRegistrationPage.class).searchForApplicationToModify(scenarioContext.getApplicationNumber());
            pageStore.get(MarriageRegistrationPage.class).clickOnEditButton();
        });
        And("^he modify application and update it$", () -> {
            pageStore.get(MarriageRegistrationPage.class).modifyAndUpdateMarriageApplication();
            String message = pageStore.get(MarriageRegistrationPage.class).isSuccesfulForModification();
            scenarioContext.setActualMessage(message);
        });
        And("^he closes the acknowledgements$", () -> {
            pageStore.get(MarriageRegistrationPage.class).closeMultipleWindows();
        });
        And("^he search applications for re issue certificate$", () -> {
            pageStore.get(MarriageRegistrationPage.class).searchMarriageApplication(scenarioContext.getApplicationNumber());
        });
        And("^he selects the application for re issue certificate$", () -> {
            pageStore.get(MarriageRegistrationPage.class).selectsReIssueCertificate();
        });
        And("^he enters the memorandum of marriage$", () -> {
            pageStore.get(MarriageRegistrationPage.class).entersMemorandumOfMarriage();
        });
        And("^he forward to commissioner$", () -> {
            String approverDetailsDataId = "commissioner1";
            ApprovalDetails approvalDetails = new ExcelReader(approvalDetailsTestDataFileName).getApprovalDetails(approverDetailsDataId);
            pageStore.get(ApprovalDetailsPage.class).enterApprovalDetails(approvalDetails);
            pageStore.get(NewSewerageConnectionPage.class).forward();
        });
        And("^he get application number and closes acknowledgement$", () -> {
            String message = pageStore.get(MarriageRegistrationPage.class).getReIssueNumber();
            String number = message.split("\\s")[5];
            scenarioContext.setActualMessage(message);
            scenarioContext.setApplicationNumber(number);
            pageStore.get(MarriageRegistrationPage.class).close();
        });

        And("^he re issue the marriage application  and close the acknowledgement$", () -> {
            pageStore.get(MarriageRegistrationPage.class).approve();
            pageStore.get(MarriageRegistrationPage.class).closeApplication();
        });
        And("^he search for above application number to collect marriage Registration fee for reissue$", () -> {
            pageStore.get(MarriageRegistrationPage.class).searchForMarriageApplicationNumberToCollect(scenarioContext.getApplicationNumber(), "reissue");
            pageStore.get(MarriageRegistrationPage.class).clickOnCollectDropdown();
        });
        And("^he collect the registration charges and closes the acknowledgement$", () -> {
            pageStore.get(NewSewerageConnectionPage.class).collectCharges();
            pageStore.get(NewSewerageConnectionPage.class).closeMultipleWindows("/mrs/registration/collectmrfee/");
        });
        And("^he closes the acknowledgement$", () -> {
            pageStore.get(MarriageRegistrationPage.class).closeApplication();
        });
        And("^print the marraige cerificate$", () -> {
            pageStore.get(MarriageRegistrationPage.class).printMarriageCertificate();
        });
    }
}
