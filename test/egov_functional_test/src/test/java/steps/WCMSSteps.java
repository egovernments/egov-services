package steps;

import cucumber.api.java.en.And;
import pages.WCMSPage;

public class WCMSSteps extends BaseSteps {

    @And("^user on WCMSDemand screen will enter all demand details$")
    public void userOnWCMSDemandScreenWillEnterAllDemandDetails() throws Throwable {
        pageStore.get(WCMSPage.class).enterDemandAndCollectionDetails();
    }
}
