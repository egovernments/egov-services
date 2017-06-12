package steps.collections;

import cucumber.api.java8.En;
import entities.collections.PaymentMethod;
import excelDataFiles.CollectionsDataReader;
import pages.DashboardPage;
import pages.collections.MiscellaneousPage;
import steps.BaseSteps;

public class MiscellaneousSteps extends BaseSteps implements En {

    public MiscellaneousSteps() {
        And("^he enters Miscellaneous header$", () -> {
            pageStore.get((MiscellaneousPage.class)).enterMiscellaneousDetails();
        });
        And("^he pays using (\\w+)$", (String paymentMode) -> {
            PaymentMethod paymentmethod = new CollectionsDataReader(collectionsTestDataFileName).getPaymentMethodDetails(paymentMode);
            pageStore.get(MiscellaneousPage.class).enterPaymentDetails(paymentmethod, paymentMode);
        });
        And("^he chooses to act upon the above receipt in drafts$", () -> {
            pageStore.get(DashboardPage.class).openApplication("Entry Fees");
        });
        And("^he submit all collections$", () -> {
            String message = pageStore.get(MiscellaneousPage.class).submitAllCollections();
            scenarioContext.setActualMessage(message);
        });
        And("^user closes the acknowledgement$", () -> {
            pageStore.get(MiscellaneousPage.class).close();
        });
        And("^he chooses to act upon the above receipt in inbox$", () -> {
            pageStore.get(DashboardPage.class).openApplication("Entry Fees");
        });
        And("^he approves all collections$", () -> {
            String message = pageStore.get(MiscellaneousPage.class).approveAllCollections();
            scenarioContext.setActualMessage(message);
        });

        And("^he search for required receipt$", () -> {
            pageStore.get(MiscellaneousPage.class).searchRequiredReceipt();
        });
        And("^he selects the required receipt$", () -> {
            pageStore.get(MiscellaneousPage.class).selectRequiredReceipt();
        });
        And("^he cancel the receipt$", () -> {
            String message = pageStore.get(MiscellaneousPage.class).cancelReceipt();
            scenarioContext.setActualMessage(message);
        });

        And("^he select the required file with bank details$", () -> {
            pageStore.get(MiscellaneousPage.class).enterBankDetails();

            String actualMessage = pageStore.get(MiscellaneousPage.class).successMessageOfRemittance();
            scenarioContext.setActualMessage(actualMessage);
        });
        And("^user will notified by payment receipt as url (.*)$", (String name) -> {
            pageStore.get(MiscellaneousPage.class).checkPaymentPage(name);
        });
    }
}
