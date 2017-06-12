package steps.employeeManagement;

import cucumber.api.java8.En;
import pages.employeeManagement.community.CommunityPage;
import steps.BaseSteps;

public class CommunitySteps extends BaseSteps implements En {

    public CommunitySteps() {

        And("^user will enter the community details for creation$", () -> {
            pageStore.get(CommunityPage.class).enterCommunityDetails();
        });
    }
}
