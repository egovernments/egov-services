package steps.ptis;

import cucumber.api.java8.En;
import entities.ptis.DemolitionDetail;
import excelDataFiles.PTISDataReader;
import pages.ptis.DemolitionPage;
import steps.BaseSteps;

public class DemolitionSteps extends BaseSteps implements En {
    public DemolitionSteps() {

        And("^he enters demolition details as (\\w+)$", (String DemolitionDataId) -> {
            DemolitionDetail demolitionDetails = new PTISDataReader(ptisTestDataFileName).getDemolitionDetails(DemolitionDataId);
            pageStore.get(DemolitionPage.class).DemolitionBlock(demolitionDetails);
        });
    }
}
