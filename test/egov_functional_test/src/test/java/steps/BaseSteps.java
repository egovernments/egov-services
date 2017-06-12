package steps;

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

    public static ScenarioContext scenarioContext;
    protected String ptisTestDataFileName = "PTISTestData";
    protected String collectionsTestDataFileName = "CollectionsTestData";
    protected String lineEstimateTestDataFileName = "lineEstimateTestData";
    protected String loginTestDataFileName = "loginTestData";
    protected String tradeLicenseTestDataFileName = "tradeLicenseTestData";
    protected String financialTestDataFileName = "financialTestData";
    protected String councilManagementTestDataFileName = "CouncilManagementTestData";
    protected String grievanceTestDataFileName = "grievancesTestData";
    protected String marriageRegistrationTestDataFileName = "MarriageRegistrationTestData";
    protected String approvalDetailsTestDataFileName = "approvalDetailsTestData";
    protected String legalCaseTestDataFileName = "legalCaseTestData";
    protected String waterChargesTestDataFileName = "waterChargesTestData";
    protected String advertisementTestDataFileName = "AdvertisementDetailsTestData";
    protected String sewerageTaxTestDataFileName = "SewerageTaxTestData";
    protected String assetTestDataFileName = "assetDetailsTestData";
    protected String eisTestDataFileName = "eisTestData";
    protected String lamsTestDataFileName = "lamsTestData";


    protected void takeScreenShot(String screenshotName) throws IOException {
        WebDriver augment = new Augmenter().augment(pageStore.getDriver());
        File file = ((TakesScreenshot) augment).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(file, new File(screenshotName + ".jpg"));
    }

}
