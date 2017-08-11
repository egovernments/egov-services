package steps;

import com.testvagrant.intents.Intent;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import utils.ScenarioContext;

import java.io.File;
import java.io.IOException;

public class BaseSteps {

    public static PageStore pageStore;
    public static Intent intent;

    public static ScenarioContext scenarioContext;
    protected String ptisTestDataFileName = "PTISTestData";

    protected void takeScreenShot(String screenshotName) throws IOException {
        WebDriver augment = new Augmenter().augment(pageStore.getDriver());
        File file = ((TakesScreenshot) augment).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(file, new File(screenshotName + ".jpg"));
    }
}
