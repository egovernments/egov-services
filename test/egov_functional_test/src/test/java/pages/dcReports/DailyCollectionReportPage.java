package pages.dcReports;

import entities.dcReports.PTReport;
import entities.dcReports.VLTReport;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.BasePage;

public class DailyCollectionReportPage extends BasePage {

    private WebDriver webDriver;

    @FindBy(id = "fromDate")
    private WebElement vltFromDate;

    @FindBy(id = "toDate")
    private WebElement vltToDate;

    @FindBy(id = "dailyCollectionReportSearchVLT")
    private WebElement vltReportSearch;

    @FindBy(name = "fromDate")
    private WebElement ptFromDate;

    @FindBy(id = "toDate")
    private WebElement ptToDate;

    @FindBy(id = "dailyCollectionReportSearch")
    private WebElement ptReportSearch;

    public DailyCollectionReportPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void enterVLTReportDetails(VLTReport vltReport) {
        enterDate(vltFromDate, vltReport.getFromDate(), webDriver);
        enterDate(vltToDate, vltReport.getToDate(), webDriver);

        vltReportSearch.click();
    }

    public void enterPTReportDetails(PTReport ptReport) {
        enterDate(ptFromDate, ptReport.getFromDate(), webDriver);
        enterDate(ptToDate, ptReport.getToDate(), webDriver);

        ptReportSearch.click();
    }

}
