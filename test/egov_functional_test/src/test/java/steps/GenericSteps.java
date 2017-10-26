package steps;

import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import pages.ConstructElement;
import pages.GenericPage;
import utils.StringExtract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class GenericSteps extends BaseSteps {

    public static List<String> dataTableStore = new ArrayList<>();
    public static int i = 0;
    public static Map<String, String> copyValues = new HashMap<>();

    @Given("^(\\w+)\\s+on\\s+(\\w+)\\s+screen\\s+(\\w+)\\s+on\\s+(\\w+)\\s+value\\s+(.*)$")
    public void consumerOnScreenPerformsActionOnElementWithValue(String consumer, String screen, String action, String element, String value) throws Exception {
        pageStore.get(GenericPage.class).buildElementForAction(screen, action, element, value);
    }

    @And("^(\\w+)\\s+on\\s+(\\w+)\\s+screen\\s+(\\w+)\\s+on\\s+(\\w+)$")
    public void consumerOnScreenPerformsActionOnElement(String consumer, String screen, String action, String element) throws Exception {
        pageStore.get(GenericPage.class).buildElementForAction(screen, action, element, "");
    }

    @And("^(\\w+)\\s+on\\s+(\\w+)\\sscreen verifies\\s+(\\w+)\\s+is\\s+(.*)$")
    public void assertElement(String consumer, String screen, String element, String action) throws Exception {
        pageStore.get(GenericPage.class).buildElementForAction(screen, action, element, "");
    }

    @And("^(\\w+)\\s+on\\s+(\\w+)\\sscreen verifies\\s+(\\w+)\\s+has\\s+(\\w+)\\s+value\\s+(.*)$")
    public void assertElementWithValue(String consumer, String screen, String element, String action, String value) throws Exception {
        pageStore.get(GenericPage.class).buildElementForAction(screen, action, element, value);
    }

    @And("^(\\w+) on (\\w+) screen performs following actions$")
    public void userOnScreenPerformsFollowingActions(String consumer, String screen, DataTable dataTable) throws Throwable {
        List<List<String>> data = dataTable.raw();
        int x = 0;

        for (List<String> aData : data) {
            String action = aData.get(x++);
            String element = aData.get(x++);
            String value = aData.get(x);
            pageStore.get(GenericPage.class).buildElementForAction(screen, action, element, value);
            x = 0;
        }
    }

    @Given("^(Intent):(.*)$")
    public void intent(String action, String intentId) throws Throwable {
        intent.run(intentId);
    }

    @Given("^(DataIntent):(.*)$")
    public void intentWithDataTable(String action, String intentId, DataTable dataTables) throws Throwable {
        List<List<String>> data = dataTables.raw();
        for (List<String> aData : data) {
            dataTableStore.addAll(aData);
        }
        intent.run(intentId);
    }

    @And("^(\\w+)\\s+on\\s+(\\w+)\\sscreen\\s+(\\w+)\\sthe\\s+(\\w+)\\s+to\\s+(\\w+)$")
    public void userOnScreenCopiesTextToScenarioContext(String consumer, String screen, String action, String element, String placeHolder) throws Throwable {

        WebElement webElement = pageStore.get(ConstructElement.class).buildElement(screen, element, "");

        if (copyValues.containsKey(placeHolder))
            copyValues.put(placeHolder, null);

        while (copyValues.get(placeHolder) == null || copyValues.get(placeHolder).equals("NA")) {
            System.out.println(copyValues.get(placeHolder));
            switch (placeHolder) {
                case "applicationNumber":
                case "SRNReqNumber":
                    copyValues.put(placeHolder, pageStore.get(StringExtract.class).getComplaintNumber(webElement));
                    break;
                case "user":
                    copyValues.put(placeHolder, webElement.getText().split("::")[0]);
                    break;
                default:
                    if (!webElement.getText().equals(""))
                        copyValues.put(placeHolder, webElement.getText());
                    else
                        copyValues.put(placeHolder, webElement.getAttribute("value"));
                    break;
            }
        }
        System.out.println(copyValues);
    }

    @And("^(\\w+)\\s+on\\s+(\\w+)\\sscreen\\s+(\\w+)\\son\\s+(\\w+)\\s+with\\s+above\\s+(.*)$")
    public void userOnScreenTypesOnApplicationSearchWithAboveApplicationNumber(String consumer, String screen, String action, String element, String value) throws Throwable {
        if (copyValues.containsKey(value))
            value = copyValues.get(value);
        if (action.equals("opens")) {
            pageStore.get(GenericPage.class).buildElementForAction(screen, "types", element, value);
            pageStore.get(GenericPage.class).openApplication(value).click();
        } else {
            pageStore.get(GenericPage.class).buildElementForAction(screen, action, element, value);
        }
    }

    @And("^(\\w+)\\s+on (\\w+) screen types on (\\w+) suggestion box with value (.*)$")
    public void selectsSuggestionBoxWithValue(String consumer, String screen, String element, String value) throws Throwable {
        if (copyValues.containsKey(value))
            value = copyValues.get(value);
        WebElement webElement = pageStore.get(ConstructElement.class).buildElement(screen, element, "");
        pageStore.get(GenericPage.class).actionOnSuggestionBox(webElement, value);
    }

    @And("^(\\w+) on (\\w+) screen will see the (.*)$")
    public void userWillSeeElement(String consumer, String screen, String element) throws Throwable {
        pageStore.get(ConstructElement.class).buildElement(screen, element, "");
    }

    @And("^(\\w+) on (\\w+) screen refresh's the webpage$")
    public void userOnHomeScreenRefreshSTheWebpage(String user, String s) throws Throwable {
        pageStore.getDriver().navigate().refresh();
    }

    @And("^(\\w+) on (\\w+) screen will wait until the page loads$")
    public void userOnHomeScreenWillWaitUntilThePageLoads(String user, String screen) throws Throwable {
        pageStore.get(ConstructElement.class).buildElement("Home", "background", "");
    }

    @And("^(\\w+) on (\\w+) screen scroll to the (\\w+)$")
    public void userOnScreenScrollToTheElement(String u, String s, String element) throws Throwable {
        WebElement webElement = pageStore.get(ConstructElement.class).buildElement(s, element, "");
        ((JavascriptExecutor) pageStore.getDriver()).executeScript("arguments[0].scrollIntoView(true);", webElement);
    }

    @And("^(\\w+) on (\\w+) screen accepts the popup$")
    public void userOnScreenAcceptsThePopup(String u, String s) throws Throwable {
        try {
            int i = 1;
            while (i > 0) {
                pageStore.getDriver().switchTo().alert().accept();
                TimeUnit.SECONDS.sleep(1);
                i++;
            }
        } catch (Exception e) {
        }
    }
}