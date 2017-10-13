package steps;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import pages.TLPage;

import static steps.BaseSteps.pageStore;

public class TLSteps extends BaseSteps {
    @And("^user on TradeLicense screen Logout$")
    public void userOnTradeLicenseScreenLogout() {
        pageStore.get(TLPage.class).TLLogout();
    }
}
