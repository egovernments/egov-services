package steps;

import cucumber.api.java.en.And;
import pages.PTISPage;

public class PTISSteps extends BaseSteps {

    @And("^user on PTISDemand screen enter all demands$")
    public void userOnPTISDemandScreenEnterAllDemands() throws Throwable {
        pageStore.get(PTISPage.class).enterDemandAndCollectionDetails();
    }
}
