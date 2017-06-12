package steps.collections;

import cucumber.api.java8.En;
import entities.collections.PaymentMethod;
import excelDataFiles.CollectionsDataReader;
import pages.collections.PropertyTaxPage;
import steps.BaseSteps;

public class PropertyTaxSteps extends BaseSteps implements En {

    public PropertyTaxSteps() {
        And("^he chooses to collect tax for above assessment number$", () -> {
            pageStore.get((PropertyTaxPage.class)).collectTaxFor(scenarioContext.getAssessmentNumber());
        });
        And("^he chooses to pay tax$", () -> {
            pageStore.get(PropertyTaxPage.class).payTax();
        });
        And("^he collect tax using (\\w+)$", (String paymentMode) -> {
            PaymentMethod paymentmethod = new CollectionsDataReader(collectionsTestDataFileName).getPaymentMethodDetails(paymentMode);
            pageStore.get(PropertyTaxPage.class).collectTax(paymentmethod, paymentMode, "Bill");
        });
    }
}
