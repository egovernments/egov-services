import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.WebDriver;
import pages.BasePage;
import steps.BaseSteps;
import steps.Hooks;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class JsonParserSteps extends BaseSteps{
    public WebDriver driver;
    public String json;

    public JsonParserSteps(WebDriver driver){this.driver = driver;}

    @When("^I read json string from a file$")
    public void iReadJsonStringFromAFile() throws Throwable {
        FileInputStream fin = new FileInputStream(new File(System.getProperty("user.dir")+"/src/test/resources/json/asset.json"));
        InputStreamReader in = new InputStreamReader(fin);
        BufferedReader bufferedReader = new BufferedReader(in);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }
        json = sb.toString();
    }

    @Then("^I parse the string and print keys and values$")
    public void iParseTheStringAndPrintKeysAndValues() throws Throwable {
        JsonParser parser = new JsonParser();
        JsonObject myobject = (JsonObject)parser.parse(json);
        System.out.println(myobject.get("definitions"));
        JsonObject asset = (JsonObject)myobject.get("definitions");
        String assetJson = asset.toString();
        System.out.println("Asset Value: "+asset.get("Asset"));
        JsonObject properties = (JsonObject)parser.parse(assetJson);
        System.out.println("Properties Value: "+properties.get("properties"));
    }
}
