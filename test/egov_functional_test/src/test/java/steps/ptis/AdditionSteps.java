package steps.ptis;

import cucumber.api.java8.En;
import entities.ptis.EditAssessmentDetails;
import entities.ptis.EditFloorDetails;
import excelDataFiles.PTISDataReader;
import pages.ptis.PropertyDetailsPage;
import steps.BaseSteps;

public class AdditionSteps extends BaseSteps implements En {
    public AdditionSteps() {

//        And("^he searches for assessment with number \"([^\"]*)\"$", (String arg0) -> {
//            pageStore.get(PropertyDetailsPage.class).searchAssessmentNumber(scenarioContext.getDataScreenAssessmentNumber());
//            pageStore.get(PropertyDetailsPage.class).search();
//        });
        And("^he updates assessment details as (\\w+)$", (String assessmentDetailsDataName) -> {
            EditAssessmentDetails assessmentDetails = new PTISDataReader(ptisTestDataFileName).getEditAssessmentDetails(assessmentDetailsDataName);
            pageStore.get(PropertyDetailsPage.class).enterEditAssessmentDetails(assessmentDetails);
        });
        And("^he enters Floor Details as (\\w+)$", (String floordetailsDataName) -> {
            // Write code here that turns the phrase above into concrete actions
            EditFloorDetails floorDetails = new PTISDataReader(ptisTestDataFileName).getEditFloorDetails(floordetailsDataName);
            pageStore.get(PropertyDetailsPage.class).enterEditFloorDetails(floorDetails);
        });
        And("^he searches for assessment with number$", () -> {
            pageStore.get(PropertyDetailsPage.class).searchAssessmentNumber(scenarioContext.getDataScreenAssessmentNumber());
            pageStore.get(PropertyDetailsPage.class).search();
        });
//        And("^he will copy application and assessment number$", () -> {
//           scenarioContext.setActualMessage(pageStore.get(PropertyDetailsPage.class).getAssessmentApplicationNo());
//            pageStore.get(PropertyAcknowledgementPage.class).close();
//        });

    }
}
