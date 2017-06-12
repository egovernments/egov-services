package pages.works;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.BasePage;

import static com.jayway.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;

public class MilestoneTrackPage extends BasePage {

    private WebDriver driver;

    @FindBy(xpath = ".//*[@id='btnsearch']")
    private WebElement searchButton;

    @FindBy(id = "resultTable_wrapper")
    private WebElement searchTableForCreate;

    @FindBy(id = "createMilestone")
    private WebElement createMilestoneButton;

    @FindBy(xpath = ".//*[@id='milestone']/div[1]/div/div[1]/div/div/div[2]/div[5]/div[2]/a")
    private WebElement loaNumberBox;

    @FindBy(id = "milestoneDetailsTbl")
    private WebElement milestoneStageTbl;

    @FindBy(id = "addRowBtn")
    private WebElement addRowButton;

    @FindBy(className = "datepicker-days")
    private WebElement calenderForm;

    @FindBy(xpath = ".//*[@id='resultTable']/tbody/tr[1]/td[1]/input")
    private WebElement radioButton;

    @FindBy(xpath = ".//*[@id='save']")
    private WebElement saveButton;

    @FindBy(id = "successMessage")
    private WebElement creationMsg;

    @FindBy(xpath = ".//*[@id='page-container']/div/div[1]/div[1]")
    private WebElement creationMsg1;

    @FindBy(css = "input[value='Close'][type='submit']")
    private WebElement closeButton;

    @FindBy(css = "input[id='workOrderNumber'][type='text']")
    private WebElement loaNumberTextBox;

    @FindBy(id = "btntrackmilestone")
    private WebElement trackMilestoneButton;

    @FindBy(css = "input[id = 'adminSanctionFromDate'][type = 'text']")
    private WebElement adminSanctionDateTextBox;

    @FindBy(id = "btncreateloa")
    private WebElement createContractorBillButton;

    @FindBy(id = "billtype")
    private WebElement billTypeBox;

    @FindBy(id = "mbRefNo")
    private WebElement mbRefNoTextBox;

    @FindBy(id = "fromPageNo")
    private WebElement fromPageNoTextBox;

    @FindBy(id = "toPageNo")
    private WebElement toPageNoTextBox;

    @FindBy(css = "input[id='mbDate'][type='text']")
    private WebElement mbDateTextBox;

    @FindBy(css = "input[id='workCompletionDate'][type='text']")
    private WebElement completionDateTextBox;

    @FindBy(css = "input[id='debitamount'][type='text']")
    private WebElement debitAmountTextBox;

    @FindBy(css = "input[id='Forward'][type='submit']")
    private WebElement forwardButton;

    @FindBy(id = "official_inbox")
    private WebElement workListTable;

    @FindBy(id = "approvalComent")
    private WebElement approvalCommentBox;

    @FindBy(css = "input[value='Approve'][type='submit']")
    private WebElement approveButton;

    public MilestoneTrackPage(WebDriver driver) {
        this.driver = driver;
    }

    public void search(String number) {
        driver.findElement(By.id("workOrderNumber")).sendKeys(number);
        clickOnButton(searchButton, driver);
    }

    public void select() {
        jsClick(radioButton, driver);
    }

    public void createMilestone() {
        clickOnButton(createMilestoneButton, driver);
    }

    public String getLoaNumber() {
        String loaNumber = getTextFromWeb(loaNumberBox, driver);
        return loaNumber;
    }

    public void enterMilestoneDetails() {
        waitForElementToBeVisible(milestoneStageTbl, driver);
        WebElement requiredRow = milestoneStageTbl.findElements(By.tagName("tr")).get(0);

        WebElement stageDescription = requiredRow.findElements(By.tagName("td")).get(1).findElement(By.name("activities[0].description"));
        stageDescription.sendKeys("Stage 1");

        WebElement stagePercentage = requiredRow.findElements(By.tagName("td")).get(2).findElement(By.name("activities[0].percentage"));
        stagePercentage.sendKeys("50");

        WebElement stageScheduleStartDate = requiredRow.findElements(By.tagName("td")).get(3).findElement(By.name("activities[0].scheduleStartDate"));
        stageScheduleStartDate.sendKeys(getCurrentDate());
        stageScheduleStartDate.clear();
        stageScheduleStartDate.sendKeys(getCurrentDate());

        WebElement stageScheduleEndDate = requiredRow.findElements(By.tagName("td")).get(4).findElement(By.name("activities[0].scheduleEndDate"));
        stageScheduleEndDate.sendKeys(getFutureDate(30));
        stageScheduleEndDate.clear();
        stageScheduleEndDate.sendKeys(getFutureDate(30));

//        enterDate(stageScheduleEndDate, getFutureDate(52), driver);
//        stageScheduleEndDate.clear();
//        enterText(stageScheduleEndDate, getFutureDate(52), driver);
//        stageScheduleEndDate.clear();
//        enterText(stageScheduleEndDate, getFutureDate(52), driver);

        waitForElementToBeClickable(addRowButton, driver);
        addRowButton.click();

        WebElement requiredRow1 = milestoneStageTbl.findElements(By.tagName("tr")).get(1);

        WebElement stageOrderNo1 = driver.findElement(By.xpath("(//*[@id='stageOrderNo'])[2]"));
        stageOrderNo1.sendKeys("2");

        WebElement stageDescription1 = requiredRow1.findElements(By.tagName("td")).get(1).findElement(By.name("activities[1].description"));
        stageDescription1.sendKeys("Stage 2");

        WebElement stagePercentage1 = requiredRow1.findElements(By.tagName("td")).get(2).findElement(By.name("activities[1].percentage"));
        stagePercentage1.sendKeys("50");

        WebElement stageScheduleStartDate1 = requiredRow1.findElements(By.tagName("td")).get(3).findElement(By.name("activities[1].scheduleStartDate"));
        stageScheduleStartDate1.sendKeys(getFutureDate(37));
        stageScheduleStartDate1.clear();
        stageScheduleStartDate1.sendKeys(getFutureDate(37));
//
//        WebElement stageScheduleEndDate1 = requiredRow1.findElements(By.tagName("td")).get(4).findElement(By.name("activities[1].scheduleEndDate"));
//        stageScheduleEndDate1.sendKeys(getFutureDate(67));
//        stageScheduleEndDate1.clear();
//        stageScheduleEndDate1.sendKeys(getFutureDate(67));

//        enterDate(stageScheduleStartDate1, getCurrentDate(), driver);
//        stageScheduleStartDate1.clear();
//        enterDate(stageScheduleStartDate1, getCurrentDate(), driver);

        WebElement stageScheduleEndDate1 = requiredRow1.findElements(By.tagName("td")).get(4).findElement(By.name("activities[1].scheduleEndDate"));
        enterText(stageScheduleEndDate1, getFutureDate(62), driver);
        stageScheduleEndDate1.clear();
        enterText(stageScheduleEndDate1, getFutureDate(62), driver);
        stageScheduleEndDate1.clear();
        enterText(stageScheduleEndDate1, getFutureDate(62), driver);
    }

    public void save() {
        clickOnButton(saveButton, driver);
    }

    public String successMessage() {
        String msg = getTextFromWeb(creationMsg, driver);
        return msg;
    }

    public void close() {
        clickOnButton(closeButton, driver);
        switchToPreviouslyOpenedWindow(driver);
    }

    public void searchUsingLoa(String number) {
        enterText(loaNumberTextBox, number, driver);
        clickOnButton(searchButton, driver);
    }

    public void selectApplication() {
        waitForElementToBeClickable(radioButton, driver);
        jsClick(radioButton, driver);

        waitForElementToBeClickable(trackMilestoneButton, driver);
        trackMilestoneButton.click();
    }

    public void enterTrackMilestoneDetails() {
        WebElement element1 = driver.findElement(By.id("tblmilestone"));
        WebElement status1 = element1.findElement(By.name("trackMilestone[0].activities[0].status"));
        WebElement element2 = element1.findElement(By.className("scheduleEndDate_0"));
        selectFromDropDown(status1, "COMPLETED", driver);
        WebElement completionDateBox1 = element1.findElement(By.name("trackMilestone[0].activities[0].completionDate"));
        enterText(completionDateBox1, element2.getText(), driver);
        WebElement status2 = element1.findElement(By.name("trackMilestone[0].activities[1].status"));
        selectFromDropDown(status2, "COMPLETED", driver);
        await().atMost(1, SECONDS);
        WebElement completionDateBox2 = element1.findElement(By.name("trackMilestone[0].activities[1].completionDate"));
        enterDate(completionDateBox2, getFutureDate(82), driver);
        WebElement reasonForDelayTextBox = element1.findElement(By.name("trackMilestone[0].activities[1].remarks"));
        enterText(reasonForDelayTextBox, "Delay", driver);
    }

    public void createContractorBill() {
        clickOnButton(createContractorBillButton, driver);
    }

    public void enterContractorBillDetails(String billType) {

        switch (billType) {
            case "part":
                selectFromDropDown(billTypeBox, "Part Bill", driver);
                break;

            case "full":
                selectFromDropDown(billTypeBox, "Final Bill", driver);
                enterDate(completionDateTextBox, getFutureDate(62), driver);
                break;
        }

        enterText(mbRefNoTextBox, "MB" + get6DigitRandomInt(), driver);
        enterText(fromPageNoTextBox, "1", driver);
        enterText(toPageNoTextBox, "10", driver);
        enterDate(mbDateTextBox, getCurrentDate(), driver);
        enterText(debitAmountTextBox, "1000", driver);
    }

    public String forwardToDEEContractorBill() {
        clickOnButton(forwardButton, driver);
        String text = getTextFromWeb(creationMsg1, driver);
        String billNumber = text.split("\\ ")[2];
        return billNumber;
    }

    public String successMessage1() {
        String msg = getTextFromWeb(creationMsg1, driver);
        return msg;
    }

    public void approve() {
        enterText(approvalCommentBox, "Approved", driver);
        clickOnButton(approveButton, driver);
    }
}
