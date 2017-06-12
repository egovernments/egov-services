package steps.AdvertisementTax;

import cucumber.api.java8.En;
import entities.ApprovalDetails;
import excelDataFiles.ExcelReader;
import pages.AdvertisementTax.AdvertisementsPage;
import pages.AdvertisementTax.LegacyAdvertisementsPage;
import pages.ApprovalDetailsPage;
import steps.BaseSteps;

public class LegacyAdvertisementsSteps extends BaseSteps implements En {

    public LegacyAdvertisementsSteps() {

        And("^he submit the application and closes the acknowledgement$", () -> {
            String applicationNumber = pageStore.get(LegacyAdvertisementsPage.class).submit();
            scenarioContext.setApplicationNumber(applicationNumber);

            String actualMessage = pageStore.get(LegacyAdvertisementsPage.class).successMessage();
            scenarioContext.setActualMessage(actualMessage);

            pageStore.get(AdvertisementsPage.class).close();
        });

        And("^he search for required file by application number$", () -> {
            pageStore.get(LegacyAdvertisementsPage.class).searchFile(scenarioContext.getApplicationNumber());

            pageStore.get(LegacyAdvertisementsPage.class).updateLegacyAd();
        });
        And("^he update the legacy advertisement and close the acknowledgement$", () -> {
            pageStore.get(LegacyAdvertisementsPage.class).update();
            pageStore.get(LegacyAdvertisementsPage.class).submit();

            String actualMsg = pageStore.get(LegacyAdvertisementsPage.class).successMessage();
            scenarioContext.setActualMessage(actualMsg);

            pageStore.get(AdvertisementsPage.class).closeMultipleWindows("/adtax/hoarding/findhoarding-for-update");
        });

        And("^he search for required file by application number for renewal$", () -> {
            pageStore.get(LegacyAdvertisementsPage.class).searchFileForRenewal(scenarioContext.getApplicationNumber());
        });

        And("^he request for renewal and forward to commissioner$", () -> {
            pageStore.get(LegacyAdvertisementsPage.class).requestForRenewal();

            String approverDetailsDataId = "commissioner1";

            ApprovalDetails approverDetails = new ExcelReader(approvalDetailsTestDataFileName).getApprovalDetails(approverDetailsDataId);
            pageStore.get(ApprovalDetailsPage.class).enterApprovalDetails(approverDetails);

            String number = pageStore.get(AdvertisementsPage.class).forward();
            scenarioContext.setApplicationNumber(number);

            String actualMsg = pageStore.get(AdvertisementsPage.class).successMessage();
            String num = actualMsg.substring(actualMsg.length() - 1);
            scenarioContext.setActualMessage(actualMsg);
            scenarioContext.setAssessmentNumber(num);

            pageStore.get(AdvertisementsPage.class).closeMultipleWindows("/adtax/hoarding/renewal-search");
        });
        And("^he enters arrear details$", () -> {
            pageStore.get(LegacyAdvertisementsPage.class).enterArrearsTaxDetails();
        });
    }
}
