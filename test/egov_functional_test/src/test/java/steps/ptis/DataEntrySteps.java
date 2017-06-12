package steps.ptis;

import cucumber.api.java8.En;
import excelDataFiles.PTISDataReader;
import org.apache.commons.lang.math.RandomUtils;
import pages.ptis.PropertyDetailsPage;
import steps.BaseSteps;

public class DataEntrySteps extends BaseSteps implements En {
    public DataEntrySteps() {
        And("^he creates a new assessment for a private residential property$", () -> {
            String assessmentNumber = "1016" + get6DigitRandomInt();
            scenarioContext.setAssessmentNumber(assessmentNumber);
            pageStore.get(PropertyDetailsPage.class).enterAssessmentNumber(assessmentNumber);

            pageStore.get(PropertyDetailsPage.class)
                    .enterPropertyHeader(new PTISDataReader(ptisTestDataFileName).getPropertyHeaderDetails("residentialPrivate"));
            pageStore.get(PropertyDetailsPage.class)
                    .enterOwnerDetails(new PTISDataReader(ptisTestDataFileName).getOwnerDetails("bimal"));
            pageStore.get(PropertyDetailsPage.class)
                    .enterPropertyAddressDetails(new PTISDataReader(ptisTestDataFileName).getPropertyAddressDetails("addressOne"));
            pageStore.get(PropertyDetailsPage.class)
                    .enterAssessmentDetails(new PTISDataReader(ptisTestDataFileName).getAssessmentDetails("assessmentNewProperty"));
            pageStore.get(PropertyDetailsPage.class)
                    .selectAmenities(new PTISDataReader(ptisTestDataFileName).getAmenties("all"));
            pageStore.get(PropertyDetailsPage.class)
                    .enterConstructionTypeDetails(new PTISDataReader(ptisTestDataFileName).getConstructionTypeDetails("defaultConstructionType"));
            pageStore.get(PropertyDetailsPage.class)
                    .enterFloorDetails(new PTISDataReader(ptisTestDataFileName).getFloorDetails("firstFloor"));
            pageStore.get(PropertyDetailsPage.class).create();
        });
    }

    private String get6DigitRandomInt() {
        return String.valueOf((100000 + RandomUtils.nextInt(900000)));
    }
}
