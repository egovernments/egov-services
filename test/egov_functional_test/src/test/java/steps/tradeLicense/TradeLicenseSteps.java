package steps.tradeLicense;

import cucumber.api.java8.En;
import entities.tradeLicense.*;
import excelDataFiles.TradeLicenseDataReader;
import pages.tradeLicense.TradeLicensePage;
import steps.BaseSteps;

public class TradeLicenseSteps extends BaseSteps implements En {
    public TradeLicenseSteps() {
        And("^he enters trade owner details of new license (\\w+)$", (String tradeDetailsData) -> {
            TradeOwnerDetails tradeOwnerDetails = new TradeLicenseDataReader(tradeLicenseTestDataFileName).getTradeOwnerDetails(tradeDetailsData);
            pageStore.get(TradeLicensePage.class).entertradeOwnerDetails(tradeOwnerDetails);
        });
        And("^he enters trade location details of new license (\\w+)$", (String tradeLocationData) -> {
            TradeLocationDetails tradelocationDetails = new TradeLicenseDataReader(tradeLicenseTestDataFileName).getTradeLocationDetails(tradeLocationData);
            pageStore.get(TradeLicensePage.class).entertradeLocationDetails(tradelocationDetails);

        });
        And("^he enters trade details of new license (\\w+)$", (String tradeDetailsData) -> {
            TradeDetails tradedetails = new TradeLicenseDataReader(tradeLicenseTestDataFileName).getTradeDetails(tradeDetailsData);
            pageStore.get(TradeLicensePage.class).entertradeDetails(tradedetails);

        });
        And("^he search existing application number$", () -> {
            pageStore.get(TradeLicensePage.class).enterApplicationNumber(scenarioContext.getApplicationNumber());
        });

        And("^he choose to payTax of applicationNumber$", () -> {
            pageStore.get(TradeLicensePage.class).chooseToPayTaxOfApplicationNumber();
        });
        And("^he enters old license number$", () -> {
            pageStore.get(TradeLicensePage.class).chooseOldTradeLicense();
        });
        And("^he copy trade application number$", () -> {
            String applicationNumber = pageStore.get(TradeLicensePage.class).getApplicationNumber();
            scenarioContext.setApplicationNumber(applicationNumber);
            System.out.println(" Apllication number " + applicationNumber);
        });
        And("^he enters fee details of legency trade license$", () -> {
            pageStore.get(TradeLicensePage.class).enterlegencyDetails();
        });
        And("^he choose a trade license for closure as (\\w+)$", (String ClosureData) -> {
            LicenseClosureDetails closureDetails = new TradeLicenseDataReader(tradeLicenseTestDataFileName).getDetailsForClosure(ClosureData);
            pageStore.get(TradeLicensePage.class).enterDetailsForClosure(closureDetails);
            String licenseNumber = pageStore.get(TradeLicensePage.class).getLicenseNumber();
            scenarioContext.setApplicationNumber(licenseNumber);
            System.out.println(" License number" + licenseNumber);
        });
        And("^he closes the acknowledgement page$", () -> {
            pageStore.get(TradeLicensePage.class).closeAcknowledgement();
        });
//        to be removed
        And("^he approves application$", () -> {
            pageStore.get(TradeLicensePage.class).applicationApproval();
        });

        And("^he generates the license certificate$", () -> {
            scenarioContext.setActualMessage(pageStore.get(TradeLicensePage.class).generateLicenseCertificate());
        });
        And("^he search trade license with application number$", () -> {
            String searchId = "searchWithApplicationNumber";
            SearchTradeDetails searchTradeDetails = new TradeLicenseDataReader(tradeLicenseTestDataFileName).getTradeSearchDetails(searchId);
            pageStore.get(TradeLicensePage.class).enterApplicationNumberReadingFromExcel(searchTradeDetails);

        });
        And("^he copies the license number and closes the acknowledgement$", () -> {
            scenarioContext.setApplicationNumber(pageStore.get(TradeLicensePage.class).getLegacyLicenseNumber());
            System.out.println("Application Number " + scenarioContext.getApplicationNumber());
        });
        And("^he choose to search with license number$", () -> {
            pageStore.get(TradeLicensePage.class).enterLicenseNumber(scenarioContext.getApplicationNumber());
        });
        And("^he choose to renew trade license$", () -> {
            pageStore.get(TradeLicensePage.class).chooseToRenewLicense();
        });
        And("^he checks total number of records$", () -> {
            pageStore.get(TradeLicensePage.class).checkNoOfRecords();
        });
        And("^he search trade license with license number$", () -> {
            String searchId = "searchWithLicenseNumber";
            SearchTradeDetails searchTradeDetails = new TradeLicenseDataReader(tradeLicenseTestDataFileName).getTradeSearchDetails(searchId);
            pageStore.get(TradeLicensePage.class).enterLicenseNumber(searchTradeDetails.getLicenseNumber());

        });
        And("^he search trade license with status \"([^\"]*)\"$", (String status) -> {
            pageStore.get(TradeLicensePage.class).enterStatus(status);
        });
        And("^he approves the closure$", () -> {
            pageStore.get(TradeLicensePage.class).closureApproval();
        });
        And("^he closes acknowledgement page$", () -> {
            pageStore.get(TradeLicensePage.class).closeAcknowledgementPage();
        });
        And("^he choose action \"([^\"]*)\"$", (String action) -> {
            pageStore.get(TradeLicensePage.class).chooseAction(action);
        });
        And("^he confirms to proceed$", () -> {
            pageStore.get(TradeLicensePage.class).confirmToProceed();
        });
        And("^he generates demand$", () -> {
            scenarioContext.setActualMessage(pageStore.get(TradeLicensePage.class).generateDemand());
        });
        And("^he copy trade license number$", () -> {
            scenarioContext.setApplicationNumber(pageStore.get(TradeLicensePage.class).getLicenseNumber());
        });
        And("^he changes trade area as \"([^\"]*)\"$", (String tradeArea) -> {
            pageStore.get(TradeLicensePage.class).changeTradeArea(tradeArea);
        });
        And("^he cancel the application$", () -> {
            pageStore.get(TradeLicensePage.class).cancelApplication();
        });
        And("^he rejects the application$", () -> {
            pageStore.get(TradeLicensePage.class).applicationRejection();
        });

        And("^he verifies the application status$", () -> {
            scenarioContext.setActualMessage(pageStore.get(TradeLicensePage.class).applicationStatus());
        });
        And("^he verifies the License active$", () -> {
            scenarioContext.setActualMessage(pageStore.get(TradeLicensePage.class).licenseStatus());
        });
        And("^he closes search screen$", () -> {
            pageStore.get(TradeLicensePage.class).closeSearchScreen();
        });
        And("^he saves the application$", () -> {
            pageStore.get(TradeLicensePage.class).saveApplication();
        });

    }
}
