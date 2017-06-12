package steps.works;

import cucumber.api.java8.En;
import pages.works.PhysicalProgressTrackerPage;
import steps.BaseSteps;

public class PhysicalProgressTrackerSteps extends BaseSteps implements En {

    public PhysicalProgressTrackerSteps() {
        And("^he search for estimate in estimate search result$", () -> {
            pageStore.get(PhysicalProgressTrackerPage.class).searchEstimate();
        });
        And("^he upload the estimate photos for physical progress track$", () -> {
            pageStore.get(PhysicalProgressTrackerPage.class).uploadEstimatePhotos();
        });
        And("^he close the acknowledgement page$", () -> {
            pageStore.get(PhysicalProgressTrackerPage.class).close();
        });
    }
}
