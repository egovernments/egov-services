package pages.ptis;

import entities.ptis.DemolitionDetail;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.BasePage;

/**
 * Created by bimal on 3/3/17.
 */
public class DemolitionPage extends BasePage {

    public WebDriver webDriver;
    @FindBy(id = "demolitionReason")
    private WebElement reasonForDemolitionTextBox;
    @FindBy(id = "surveyNumber")
    private WebElement surveyNumberTextBox;
    @FindBy(id = "pattaNumber")
    private WebElement pattaNumberTextBox;
    @FindBy(id = "marketValue")
    private WebElement marketValueTextBox;
    @FindBy(id = "currentCapitalValue")
    private WebElement capitalValueTextBox;
    @FindBy(id = "northBoundary")
    private WebElement NorthTextBox;
    @FindBy(id = "eastBoundary")
    private WebElement EastTextBox;
    @FindBy(id = "westBoundary")
    private WebElement WestTextBox;
    @FindBy(id = "southBoundary")
    private WebElement SouthTextBox;

    public DemolitionPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void DemolitionBlock(DemolitionDetail demolitionDetail) {
        enterText(reasonForDemolitionTextBox, demolitionDetail.getReasonForDemolition(), webDriver);
        enterText(surveyNumberTextBox, demolitionDetail.getSurveyNumber(), webDriver);
        enterText(pattaNumberTextBox, demolitionDetail.getPattaNumber(), webDriver);
        enterText(marketValueTextBox, demolitionDetail.getMarketValue(), webDriver);
        enterText(capitalValueTextBox, demolitionDetail.getCapitalValue(), webDriver);
        enterText(NorthTextBox, demolitionDetail.getNorth(), webDriver);
        enterText(EastTextBox, demolitionDetail.getEast(), webDriver);
        enterText(WestTextBox, demolitionDetail.getWest(), webDriver);
        enterText(SouthTextBox, demolitionDetail.getSouth(), webDriver);

    }


}
