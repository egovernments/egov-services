package steps.AdvertisementTax;

import cucumber.api.java8.En;
import entities.ApprovalDetails;
import entities.advertisementTax.AdvertisementDetails;
import entities.advertisementTax.LocalityDetails;
import entities.advertisementTax.PermissionDetails;
import entities.advertisementTax.StructureDetails;
import excelDataFiles.AdvertisementTaxDataReader;
import excelDataFiles.ExcelReader;
import pages.AdvertisementTax.AdvertisementsPage;
import pages.ApprovalDetailsPage;
import steps.BaseSteps;

public class AdvertisementsSteps extends BaseSteps implements En {

    public AdvertisementsSteps() {

        And("^he forwards and closes the acknowledgement$", () -> {
            String number = pageStore.get(AdvertisementsPage.class).forward();
            scenarioContext.setApplicationNumber(number);

            String actualMsg = pageStore.get(AdvertisementsPage.class).successMessage();
            scenarioContext.setActualMessage(actualMsg);

            pageStore.get(AdvertisementsPage.class).close();

        });

        And("^he approves the advertisement application$", () -> {
            pageStore.get(AdvertisementsPage.class).approverComment();
            pageStore.get(AdvertisementsPage.class).approve();

            String advertisementNumber = pageStore.get(AdvertisementsPage.class).getAdvertisementNumber();
            scenarioContext.setApplicationNumber(advertisementNumber);

            String actualMsg = pageStore.get(AdvertisementsPage.class).successMessage();
            scenarioContext.setActualMessage(actualMsg);

            pageStore.get(AdvertisementsPage.class).close();
        });

        And("^he search and select the required advertisement$", () -> {
            pageStore.get(AdvertisementsPage.class).searchAndSelect(scenarioContext.getApplicationNumber());
        });
        And("^he view and close the acknowledgement$", () -> {
            pageStore.get(AdvertisementsPage.class).closeMultipleWindows("/adtax/hoarding/adtax-search");
        });

        And("^he search advertisement by advertisement number$", () -> {
            pageStore.get(AdvertisementsPage.class).searchByAdvertisementNumber(scenarioContext.getApplicationNumber());
        });
        And("^he choose advertisement for collecting advertisement tax$", () -> {
            pageStore.get(AdvertisementsPage.class).collectAdvertisementTax();

            pageStore.get(AdvertisementsPage.class).closeMultiple("/adtax/hoarding/search");
        });

        And("^he enter details for agency creation$", () -> {
            String name = pageStore.get(AdvertisementsPage.class).enterAgencyDetails();
            scenarioContext.setAssessmentNumber(name);
            System.out.println("\n" + name);
        });

        And("^he choose to collect advertisement tax by agency wise$", () -> {
            pageStore.get(AdvertisementsPage.class).searchByAgency(scenarioContext.getAssessmentNumber());

        });

        And("^he selects the agency for Tax/Fees collection$", () -> {
            pageStore.get(AdvertisementsPage.class).selectAdvertisementAgency();
        });

        And("^he choose to collect advertisement tax$", () -> {
            pageStore.get(AdvertisementsPage.class).collectAdvertisementTaxByAgency();

            pageStore.get(AdvertisementsPage.class).closeMultiple("/adtax/hoarding/search");
        });

        And("^he submit the details and closes acknowledgement$", () -> {
            pageStore.get(AdvertisementsPage.class).submit();

            String message = pageStore.get(AdvertisementsPage.class).agencyCreationMessage();
            scenarioContext.setActualMessage(message);

            pageStore.get(AdvertisementsPage.class).CloseAgency();
        });

        And("^he enter details for search agency$", () -> {
            pageStore.get(AdvertisementsPage.class).searchAgency(scenarioContext.getAssessmentNumber());
        });

        And("^he view and closes the acknowledgement$", () -> {
            pageStore.get(AdvertisementsPage.class).CloseAgencySearch();
        });

        And("^he search for advertisement for deactivate$", () -> {
            pageStore.get(AdvertisementsPage.class).searchAdvertisementForDeactivate(scenarioContext.getApplicationNumber());
        });

        And("^he deactivates the advertisement with remarks and date$", () -> {
            pageStore.get(AdvertisementsPage.class).deactivatesAdvertisement();
            String message = pageStore.get(AdvertisementsPage.class).successMessageForDeactivation();
            scenarioContext.setActualMessage(message);
        });

        And("^user closes the acknowledgement pages$", () -> {
            pageStore.get(AdvertisementsPage.class).closeMultipleWindowsForDeactivateadvertisement("/adtax/deactivate/search");
        });

        And("^he enters advertisement details as (\\w+)$", (String advertisementDetailDataId) -> {
            AdvertisementDetails advertisementDetails = new AdvertisementTaxDataReader(advertisementTestDataFileName).getAdvertisementDetails(advertisementDetailDataId);
            pageStore.get(AdvertisementsPage.class).enterAdvertisementDetails1(advertisementDetails);

        });

        And("^he enters permission details as (\\w+)$", (String permissionDetailsDataId) -> {
            PermissionDetails permissionDetails = new AdvertisementTaxDataReader(advertisementTestDataFileName).getPermissionDetails(permissionDetailsDataId);
            pageStore.get(AdvertisementsPage.class).enterPermissionDetails1(permissionDetails);
        });

        And("^he enters locality details as (\\w+)$", (String localityDetailsDataId) -> {
            LocalityDetails localityDetails = new AdvertisementTaxDataReader(advertisementTestDataFileName).getLocalityDetails(localityDetailsDataId);
            pageStore.get(AdvertisementsPage.class).enterLocalityDetails1(localityDetails);
        });

        And("^he enters structure details as (\\w+)$", (String structureDetailsDataId) -> {
            StructureDetails structureDetails = new AdvertisementTaxDataReader(advertisementTestDataFileName).getStructureDetails(structureDetailsDataId);
            pageStore.get(AdvertisementsPage.class).enterStructureDetails1(structureDetails);
        });

        And("^he enter approver details as (\\w+)$", (String approverDetailsDataId) -> {
            ApprovalDetails approverDetails = new ExcelReader(approvalDetailsTestDataFileName).getApprovalDetails(approverDetailsDataId);
            pageStore.get(ApprovalDetailsPage.class).enterApprovalDetails(approverDetails);
        });

        And("^he enter agency name$", () -> {
            pageStore.get(AdvertisementsPage.class).enterAgencyDetailsForCreationAdvertisement(scenarioContext.getAssessmentNumber());
        });
    }
}
