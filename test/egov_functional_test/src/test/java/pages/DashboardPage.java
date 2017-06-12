package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.jayway.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;

public class DashboardPage extends BasePage {
    private WebDriver driver;

    @FindBy(id = "searchtree")
    private WebElement searchTreeTextBox;

    @FindBy(css = ".list a")
    private List<WebElement> searchResults;

    @FindBy(className = "profile-name")
    private WebElement profileNameLink;

    @FindBy(linkText = "Sign Out")
    private WebElement signOutLink;

    @FindBy(linkText = "Cheque Assignment")
    private List<WebElement> chequeAssignment;

    @FindBy(linkText = "RTGS Assignment")
    private WebElement rtgsAssignment;

    @FindBy(css = "li[class='dropdown'] a[data-work='worklist']")
    private WebElement officialInboxTable;

    @FindBy(css = "li[class='dropdown'] a[data-work='drafts']")
    private WebElement officialDraftsTable;

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
    }

    private void searchFor(String value) {
        enterText(searchTreeTextBox, value, driver);
    }

    public void logOut() {
        clickOnButton(profileNameLink, driver);
        jsClick(signOutLink, driver);
    }

    public void chooseForModeOFAssignment(String mode) {

        if (mode.equalsIgnoreCase("cheque")) {
            searchFor("Cheque Assignment");
            waitForElementToBeClickable(chequeAssignment.get(0), driver);
            chequeAssignment.get(0).click();
            switchToNewlyOpenedWindow(driver);
        } else {
            searchFor("RTGS Assignment");
            clickOnButton(rtgsAssignment, driver);
            switchToNewlyOpenedWindow(driver);
        }
    }

    public void chooseScreen(String screenName) {
        searchFor(screenName);
        waitForElementToBePresent(By.cssSelector(".list a"), driver);
        searchResults.stream().filter(searchResult -> searchResult.getText().equalsIgnoreCase(screenName)).findFirst().get().click();
        switchToNewlyOpenedWindow(driver);
    }

    public void chooseScreen(String screenName, String condition) {
        searchFor(screenName);
        waitForElementToBePresent(By.cssSelector(".list a"), driver);
        Optional<WebElement> href = searchResults.stream().filter(searchResult -> {
            return searchResult.getText().equalsIgnoreCase(screenName) && searchResult.getAttribute("href").contains(condition);
        }).findFirst();
        if (href.isPresent()) {
            href.get().click();
        }
        switchToNewlyOpenedWindow(driver);
    }

    public void openApplication(String number) {
        driver.navigate().refresh();
        getApplicationRow(number).click();
        switchToNewlyOpenedWindow(driver);
    }

    private WebElement getApplicationRow(String number) {
        List<WebElement> totalRows;
        try {
            await().atMost(40, SECONDS).until(() -> driver.findElements(By.cssSelector("[id='official_inbox'] tr td")).size() > 1);
            totalRows = driver.findElement(By.id("official_inbox")).findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
            for (WebElement applicationRow : totalRows) {
                if (applicationRow.findElements(By.tagName("td")).get(4).getText().contains(number)) {
                    return applicationRow;
                }
            }
            throw new RuntimeException("No application row found in Inbox -- " + number);
        } catch (Exception e) {
            clickOnButton(officialDraftsTable, driver);
            await().atMost(40, SECONDS).until(() -> driver.findElements(By.cssSelector("[id='official_drafts'] tr td")).size() > 1);
            totalRows = driver.findElement(By.id("official_drafts")).findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
            for (WebElement applicationRow : totalRows) {
                if (applicationRow.findElements(By.tagName("td")).get(4).getText().contains(number))
                    return applicationRow;
            }
            throw new RuntimeException("No application row found in Inbox and Drafts -- " + number);
        }
    }
}


