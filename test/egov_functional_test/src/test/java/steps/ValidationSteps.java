package steps;

import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import pages.GenericPage;

import java.util.List;

public class ValidationSteps extends BaseSteps {

    @And("^(\\w+) on (\\w+) screen validates$")
    public void userOnPTISDataEntryScreenValidates(String consumer, String screen, DataTable dataTable) throws Throwable {
        List<List<String>> data = dataTable.raw();
        int x = 0;

        for (List<String> aData : data) {
            String action = aData.get(x++);
            String element = aData.get(x++);
            String inValidData = aData.get(x++);
            String inValidMessage = aData.get(x++);
            String validData = aData.get(x);

            switch (inValidData) {
                case "NA":
                    pageStore.get(GenericPage.class).performsAction(consumer, screen, action, element, validData);
                    break;
                default:
                    pageStore.get(GenericPage.class).performsAction(consumer, screen, action, element, inValidData);
                    boolean check = pageStore.get(GenericPage.class).checkValidDataEnteredOrNot(inValidMessage);

                    if (check) {
                        pageStore.get(GenericPage.class).performsAction(consumer, screen, action, element, validData);
                    }
            }
            x = 0;
        }
    }
}
