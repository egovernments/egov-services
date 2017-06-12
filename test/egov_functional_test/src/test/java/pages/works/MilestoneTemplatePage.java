package pages.works;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.BasePage;

import java.util.List;

public class MilestoneTemplatePage extends BasePage {

    private WebDriver driver;

    @FindBy(css = "input[name ='code'][type = 'text']")
    private WebElement templateCodeBox;

    @FindBy(xpath = ".//*[@id='name']")
    private WebElement templateNameTextBox;

    @FindBy(id = "templateDescription")
    private WebElement templateDescriptionTextBox;

    @FindBy(xpath = ".//*[@id='typeOfWork']")
    private WebElement typeOfWorkBox;

    @FindBy(xpath = ".//*[@id='subType']")
    private WebElement subTypeOfWorkBox;

    @FindBy(id = "temptActvRow")
    private WebElement addTemplateActivityButton;

    @FindBy(className = "yui-dt-data")
    private WebElement stageTable;

    @FindBy(css = "input[value='Save'][type='submit']")
    private WebElement saveButton;

    @FindBy(css = "input[id='closeButton'][value='Close']")
    private WebElement closeButton;

    @FindBy(xpath = ".//*[@id='workType']")
    private WebElement typeOfWorkForViewBox;

    @FindBy(css = "input[value='Search'][type='submit']")
    private WebElement searchButton;

    @FindBy(id = "currentRow")
    private WebElement searchTable;

    @FindBy(xpath = "(//*[@id='currentRow']/tbody/tr/td/a)[last()]")
    private WebElement requiredRowForView;

    @FindBy(css = "input[value='Modify'][type='button']")
    private WebElement modifyButton;

    @FindBy(css = "input[value='Modify'][type='submit']")
    private WebElement modifyButtonAfterModication;

    @FindBy(css = "div[id='msgsDiv'][class='new-page-header']")
    private WebElement creationMsg;

    @FindBy(xpath = "(.//*[@id='milestoneTemplate-searchDetails']/div[4]/div[1]/div[2]/span[2]/a)[last()]")
    private WebElement lastPageLink;

    public MilestoneTemplatePage(WebDriver driver) {
        this.driver = driver;
    }

    public String successMessage() {
        String msg = getTextFromWeb(creationMsg, driver);
        return msg;
    }

    public void enterMilestoneTemplateDetails() {

        enterText(templateCodeBox, "TC" + get6DigitRandomInt(), driver);
        enterText(templateNameTextBox, "Testing", driver);
        enterText(templateDescriptionTextBox, "Test-automation for the project", driver);
        selectFromDropDown(typeOfWorkBox, "Roads, Drains, Bridges and Flyovers", driver);
//     waitForElementToBePresent(By.cssSelector("select[id='subType'] option[value='5']"),driver);
        selectFromDropDown(subTypeOfWorkBox, "Roads", driver);
        clickOnButton(addTemplateActivityButton, driver);
        List<WebElement> totalRows = stageTable.findElement(By.tagName("tr")).findElements(By.tagName("td"));
        WebElement stageOrderTextBox = totalRows.get(0).findElement(By.className("slnowk"));
        enterText(stageOrderTextBox, "1", driver);
        WebElement stageDescriptionTextBox = totalRows.get(1).findElement(By.className("selectmultilinewk"));
        enterText(stageDescriptionTextBox, "Testing for Roads", driver);
        WebElement stagePercentageTextBox = totalRows.get(2).findElement(By.className("selectamountwk"));
        enterText(stagePercentageTextBox, "100", driver);
    }

    public void save() {
        clickOnButton(saveButton, driver);
    }

    public void close() {
        clickOnButton(closeButton, driver);
        switchToPreviouslyOpenedWindow(driver);
    }

    public void enterMilestoneTemplateDetailsForView() {
        selectFromDropDown(typeOfWorkForViewBox, "Roads, Drains, Bridges and Flyovers", driver);
        clickOnButton(searchButton, driver);
    }

    public void selectTheRequiredTemplate() {
        boolean isPresent = driver.findElements(By.xpath("(.//*[@id='milestoneTemplate-searchDetails']/div[4]/div[1]/div[2]/span[2]/a)[last()]")).size() > 0;
        if (isPresent) {
            clickOnButton(lastPageLink, driver);
        }
        clickOnButton(requiredRowForView, driver);
    }

    public void selectTheRequiredTemplateToModify() {

        boolean isPresent = driver.findElements(By.xpath("(.//*[@id='milestoneTemplate-searchDetails']/div[4]/div[1]/div[2]/span[2]/a)[last()]")).size() > 0;

        if (isPresent) {
            waitForElementToBeVisible(lastPageLink, driver);
            lastPageLink.click();
        }

        waitForElementToBeVisible(searchTable, driver);
        List<WebElement> totalRows = searchTable.findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
//        System.out.println("Rows:"+totalRows.size());
        WebElement requiredRow = totalRows.get(totalRows.size() - 1);
        WebElement element = requiredRow.findElements(By.tagName("td")).get(0).findElement(By.id("radio"));
        jsClick(element, driver);
        clickOnButton(modifyButton, driver);
        switchToNewlyOpenedWindow(driver);
    }

    public void modifyTheMileStoneTemplateSelected() {

        waitForElementToBeVisible(stageTable, driver);
        List<WebElement> totalRows = stageTable.findElement(By.tagName("tr")).findElements(By.tagName("td"));

        WebElement stagePercentageTextBox = totalRows.get(2).findElement(By.className("selectamountwk"));
        stagePercentageTextBox.clear();
        enterText(stagePercentageTextBox, "80", driver);

        clickOnButton(addTemplateActivityButton, driver);
        WebElement requiredRow = stageTable.findElements(By.tagName("tr")).get(1);

        WebElement stageOrderTextBox2 = requiredRow.findElements(By.tagName("td")).get(0).findElement(By.id("stageOrderNoyui-rec1"));
        enterText(stageOrderTextBox2, "2", driver);

        WebElement stageDescriptionTextBox2 = requiredRow.findElements(By.tagName("td")).get(1).findElement(By.id("descriptionyui-rec1"));
        enterText(stageDescriptionTextBox2, "Testing for modification", driver);

        WebElement stagePercentageTextBox2 = requiredRow.findElements(By.tagName("td")).get(2).findElement(By.id("percentageyui-rec1"));
        enterText(stagePercentageTextBox2, "20", driver);
        clickOnButton(modifyButtonAfterModication, driver);
    }

    public void closeMultiple() {
        clickOnButton(closeButton, driver);

        for (String winHandle : driver.getWindowHandles()) {
            if (driver.switchTo().window(winHandle).getTitle().equals("eGov Works Search Milestone Template")) {
                break;
            }
        }
        selectFromDropDown(typeOfWorkForViewBox, "--------Select--------", driver);
        clickOnButton(searchButton, driver);
        close();
    }
}
