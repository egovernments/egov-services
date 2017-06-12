package steps.works;

import cucumber.api.java8.En;
import pages.works.MilestoneTemplatePage;
import steps.BaseSteps;

public class MilestoneTemplateSteps extends BaseSteps implements En {

    public MilestoneTemplateSteps() {

        And("^he enters the milestone template creation details$", () -> {
            pageStore.get(MilestoneTemplatePage.class).enterMilestoneTemplateDetails();
        });
        And("^he save the file and closes the acknowledgement$", () -> {
            pageStore.get(MilestoneTemplatePage.class).save();
            String msg = pageStore.get(MilestoneTemplatePage.class).successMessage();
            scenarioContext.setActualMessage(msg);
            pageStore.get(MilestoneTemplatePage.class).close();
        });

        And("^he enters the details for search$", () -> {
            pageStore.get(MilestoneTemplatePage.class).enterMilestoneTemplateDetailsForView();
        });
        And("^he selects the required template$", () -> {
            pageStore.get(MilestoneTemplatePage.class).selectTheRequiredTemplate();
        });
        And("^he views and closes the acknowledgement$", () -> {
            pageStore.get(MilestoneTemplatePage.class).close();
        });

        And("^he select the required template for modification$", () -> {
            pageStore.get(MilestoneTemplatePage.class).selectTheRequiredTemplateToModify();
        });
        And("^he modifies the template and closes the acknowledgement$", () -> {
            pageStore.get(MilestoneTemplatePage.class).modifyTheMileStoneTemplateSelected();
            String msg = pageStore.get(MilestoneTemplatePage.class).successMessage();
            scenarioContext.setActualMessage(msg);
            pageStore.get(MilestoneTemplatePage.class).closeMultiple();
        });
    }
}
