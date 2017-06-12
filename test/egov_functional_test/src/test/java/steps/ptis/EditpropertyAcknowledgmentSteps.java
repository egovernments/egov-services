package steps.ptis;

import cucumber.api.java.en.Then;
import pages.ptis.PropertyAcknowledgementPage;

import static steps.BaseSteps.pageStore;

public class EditpropertyAcknowledgmentSteps {

    @Then("^edit property details get saved successfully$")
    public void editPropertyDetailsGetSavedSuccessfully() throws Throwable {
        pageStore.get(PropertyAcknowledgementPage.class).close();
    }

}
