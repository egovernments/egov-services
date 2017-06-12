package pages.assetManagement;

import entities.assetManagement.assetService.HeaderDetails;
import entities.assetManagement.assetService.LocationDetails;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.BasePage;

import java.util.List;
import java.util.Random;

import static com.jayway.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;

public class AssetServicePage extends BasePage {

    // Header Details Locators
    @FindBy(id = "department")
    private WebElement departmentSelectBox;

    @FindBy(id = "assetCategory")
    private WebElement assetCategorySelectBox;

    @FindBy(id = "dateOfCreation")
    private WebElement creationDate;

    @FindBy(id = "dateOfCreation")
    private WebElement dateOfCreationTextBox;

    @FindBy(id = "description")
    private WebElement descriptionTextBox;

    @FindBy(id = "name")
    private WebElement assetNameTextBox;

    @FindBy(id = "modeOfAcquisition")
    private WebElement modeOfAcquisitionSelectBox;

    @FindBy(css = "[class='col-xs-2'] button")
    private WebElement assetReferenceSearchButton;

    @FindBy(id = "refSet.assetCategory")
    private WebElement assetReferenceCategorySelectBox;

    @FindBy(css = "[class='row text-center'] button[class='btn btn-submit']")
    private WebElement assetReferenceSubmitButton;

    @FindBy(css = "[id='tblRef'] tr td button")
    private List<WebElement> assetReferenceTableRows;

    // Location Details Locators
    @FindBy(id = "locality")
    private WebElement localitySelectBox;

    @FindBy(id = "revenueWard")
    private WebElement revenueWardSelectBox;

    @FindBy(id = "block")
    private WebElement blockNumberSelectBox;

    @FindBy(id = "street")
    private WebElement streetSelectBox;

    @FindBy(id = "electionWard")
    private WebElement electionWardNumberSelectBox;

    @FindBy(id = "doorNo")
    private WebElement doorNoTextBox;

    @FindBy(id = "zone")
    private WebElement zoneNumberSelectBox;

    @FindBy(id = "pinCode")
    private WebElement pinCodeTextBox;

    // Land Category Details
    @FindBy(css = "input[name='Land Register Number']")
    private WebElement landRegisterNumberTextBox;

    @FindBy(css = "select[name='OSR Land']")
    private WebElement osrLandSelectBox;

    @FindBy(css = "select[name='Is it Fenced']")
    private WebElement isItFencedSelectBox;

    @FindBy(css = "select[name='Land Type']")
    private WebElement landTypeSelectBox;

    @FindBy(css = "select[name='Unit of Measurement']")
    private WebElement unitOfMeasurementSelectBox;

    @FindBy(css = "[name='Government order number']")
    private WebElement governmentOrderNumberTextBox;

    @FindBy(css = "[name='Collector Order Number']")
    private WebElement collectorOrderNumberTextBox;

    @FindBy(css = "[name='Council Resolution Number']")
    private WebElement councilResolutionNumberTextBox;

    @FindBy(css = "[name='Award Number']")
    private WebElement awardNubmerTextBox;

    // Category Market Details
    @FindBy(css = "[name='Total Square feet area']")
    private WebElement totalSquareFeetAreaTextBox;

    // Category Kalyana Mandapam Details
//    @FindBy(css = "[name='Kalyana Mandapam Name']")
//    private WebElement kalyanaMandapamNameTextBox;

    // Lakes And Ponds Details
    @FindBy(css = "[name='Area (acre/sqmtr)']")
    private WebElement lakesAndPondsAreaTextBox;

    // Category Parking Space Details
    @FindBy(css = "[name='Total Square feet area']")
    private WebElement parkingSpaceTotalSquareFeetArea;

    // Category Slaughter details
    @FindBy(css = "[name='Total Square feet area']")
    private WebElement slaughterHouseTotalSquareFeet;

    // Category Usufruct Details
    @FindBy(css = "[name='Usufruct Name']")
    private WebElement ussfructNameTextBox;

    // Category Fish Tank Details
    @FindBy(css = "[name='Fish Tank Name']")
    private WebElement fishTankNameTextBox;

    // Category Park Details
    @FindBy(css = "[name='Total Square feet area']")
    private WebElement parkTotalSquareFeet;

    // Roads Details
    @FindBy(css = "[name='Road type']")
    private WebElement roadTypeSelectBox;

    // Shopping Complex Details
    @FindBy(css = "[name='Shopping Complex No.']")
    private WebElement shoppingComplexNumberTextBox;

    @FindBy(css = "[name='No. of Floors']")
    private WebElement noOfFloorsTextBox;

    @FindBy(css = "[name='Total No. of Shops']")
    private WebElement noOfShopsTextBox;

    // Category Community
    @FindBy(css = "[name='Community toilet complex Name']")
    private WebElement communityToiletComplexNameTextBox;

    // Asset Status Details Locators
    @FindBy(id = "status")
    private WebElement statusSelectBox;

    @FindBy(css = "button[type='submit']")
    private WebElement createAssetButton;

    // Search And Modify Asset Locators
    @FindBy(css = "[id='code']")
    private WebElement applicationCodeTextBox;

    @FindBy(id = "assetCategory")
    private WebElement searchAssetCategorySelect;

    @FindBy(css = "[class='text-center'] [class='btn btn-submit']")
    private WebElement searchOrUpdateButton;

    @FindBy(css = "[id='agreementSearchResultTableBody'] tr")
    private WebElement assetUpdateActionButton;

    @FindBy(css = "[class='text-center'] [class='btn btn-close']")
    private WebElement closeButton;

    @FindBy(css = "[class='land-table table-responsive'] tbody input")
    private List<WebElement> amenties;

    @FindBy(css = "[class='btn btn-primary']")
    private WebElement amentiesAddButton;

    private WebDriver webDriver;

    public AssetServicePage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void enterHeaderDetails(HeaderDetails headerDetails) {
        selectFromDropDown(departmentSelectBox, headerDetails.getDepartment(), webDriver);
        selectFromDropDown(assetCategorySelectBox, headerDetails.getAssetCategory(), webDriver);
        enterDate(creationDate, getCurrentDate(), webDriver);
        enterText(descriptionTextBox, "Description for " + headerDetails.getAssetCategory() + " Asset", webDriver);
        enterText(assetNameTextBox, "Asset: " + headerDetails.getAssetCategory(), webDriver);
        selectFromDropDown(modeOfAcquisitionSelectBox, headerDetails.getModeOfAcquisition(), webDriver);

        // Asset Reference Details
        clickOnButton(assetReferenceSearchButton, webDriver);
        selectFromDropDown(assetReferenceCategorySelectBox, headerDetails.getAssetCategory(), webDriver);
        clickOnButton(assetReferenceSubmitButton, webDriver);

        // If there is no reference application for the present category it skips the selection of application and continue the flow.
        if (webDriver.findElements(By.cssSelector("[id='tblRef'] tr td button")).size() == 0) {
            clickOnButton(webDriver.findElements(By.cssSelector("[class='btn btn-default']")).get(0), webDriver);
        } else {
            await().atMost(10, SECONDS).until(() -> webDriver.findElements(By.cssSelector("[id='tblRef'] tr td button")).size() > 0);
            if (assetReferenceTableRows.size() == 1) {
                clickOnButton(webDriver.findElement(By.cssSelector("[id='tblRef'] tr td button[class='btn btn-close']")), webDriver);
            } else {
                if (assetReferenceTableRows.size() > 10) {
                    clickOnButton(assetReferenceTableRows.get(new Random().nextInt(9 - 0) + 0), webDriver);
                } else {
                    System.out.println("=========="+assetReferenceTableRows.size());
                    clickOnButton(assetReferenceTableRows.get(new Random().nextInt(assetReferenceTableRows.size() - 0) + 0), webDriver);
                }
            }
        }
    }

    public void enterLocationDetails(LocationDetails locationDetails) {
        selectFromDropDown(localitySelectBox, locationDetails.getLocality(), webDriver);
//        enterText(webDriver.findElement(By.cssSelector("input[id='description']")), "Description", webDriver);
//        enterText(webDriver.findElement(By.cssSelector("input[type='text'][name='Shop Details']")), "Testing", webDriver);
    }

    public void enterCategoryDetails(String categoryDetails) {
        switch (categoryDetails) {
            case "Land":

                enterText(landRegisterNumberTextBox, "LReg_" + get6DigitRandomInt(), webDriver);
                selectFromDropDown(osrLandSelectBox, "Yes", webDriver);
                selectFromDropDown(isItFencedSelectBox, "Yes", webDriver);
                selectFromDropDown(landTypeSelectBox, "Hold", webDriver);
                selectFromDropDown(unitOfMeasurementSelectBox, "sq. ft.", webDriver);
                enterText(governmentOrderNumberTextBox, "GOV_" + get6DigitRandomInt(), webDriver);
                enterText(collectorOrderNumberTextBox, "CO_" + get6DigitRandomInt(), webDriver);
                enterText(councilResolutionNumberTextBox, "CRO_" + get6DigitRandomInt(), webDriver);
                enterText(awardNubmerTextBox, "A_" + get6DigitRandomInt(), webDriver);
                break;

            case "market":
                enterText(totalSquareFeetAreaTextBox, get6DigitRandomInt().substring(0, 4), webDriver);
                break;

            case "kalyanaMandapam":
                enterText(webDriver.findElement(By.cssSelector("[name='Total Square Feet Area']")), get6DigitRandomInt().substring(0, 4), webDriver);
                enterText(amenties.get(0), "AC", webDriver);
                enterText(amenties.get(1), "10", webDriver);
                clickOnButton(amentiesAddButton, webDriver);
                enterText(amenties.get(2), "Fans", webDriver);
                enterText(amenties.get(3), "40", webDriver);
                break;

            case "lakesAndPonds":
                enterText(lakesAndPondsAreaTextBox, get6DigitRandomInt().substring(0, 4), webDriver);
                break;

            case "roads":
                selectFromDropDown(roadTypeSelectBox, "concrete road", webDriver);
                break;

            case "parkingSpace":
                enterText(parkingSpaceTotalSquareFeetArea, get6DigitRandomInt().substring(0, 5), webDriver);
                break;

            case "slaughterHouse":
                enterText(slaughterHouseTotalSquareFeet, get6DigitRandomInt().substring(0, 5), webDriver);
                break;

            case "usufruct":
                enterText(webDriver.findElement(By.cssSelector("[name='Total Square feet area']")), get6DigitRandomInt().substring(0, 4), webDriver);
//                enterText(ussfructNameTextBox, "abcd", webDriver);
                break;

            case "fishTank":
                enterText(fishTankNameTextBox, "abcd", webDriver);
                break;

            case "parks":
                enterText(parkTotalSquareFeet, get6DigitRandomInt().substring(0, 5), webDriver);
                break;

            case "shopping":
                enterText(shoppingComplexNumberTextBox, String.valueOf(new Random().nextInt((9 - 4) + 1) + 1), webDriver);
                int noOfFloors = new Random().nextInt((9 - 4) + 1) + 1;
                System.out.println("=================" + noOfFloors);
                int noOfRooms = noOfFloors * 5;
                enterText(noOfFloorsTextBox, String.valueOf(noOfFloors), webDriver);
                enterText(noOfShopsTextBox, String.valueOf(noOfRooms), webDriver);
                enterText(amenties.get(0), "1", webDriver);
                enterText(amenties.get(1), String.valueOf(noOfRooms / noOfFloors), webDriver);

                break;

            case "community":
                enterText(communityToiletComplexNameTextBox, "abcd", webDriver);
                break;
        }
    }

    public void enterAssetStatusDetails(String assetStatus) {
        selectFromDropDown(statusSelectBox, assetStatus, webDriver);
        clickOnButton(createAssetButton, webDriver);
        switchToNewlyOpenedWindow(webDriver);
    }

    public String getAssetServiceNumber() {
        await().atMost(10, SECONDS).until(() -> webDriver.findElements(By.cssSelector("b[style='font-weight: bold;']")).size() > 1);
        String number = getTextFromWeb(webDriver.findElements(By.cssSelector("b[style='font-weight: bold;']")).get(1), webDriver);
        webDriver.close();
        switchToPreviouslyOpenedWindow(webDriver);
        return number;
    }

    public void searchApplicationBasedOnCategory(String details, String applicationNumber) {
        enterText(applicationCodeTextBox, applicationNumber, webDriver);
        selectFromDropDown(searchAssetCategorySelect, details, webDriver);
        clickOnButton(searchOrUpdateButton, webDriver);
        await().atMost(10, SECONDS).until(() -> webDriver.findElements(By.cssSelector("[id='agreementSearchResultTableBody'] tr")).size() > 0);
        clickOnButton(assetUpdateActionButton, webDriver);
        switchToNewlyOpenedWindow(webDriver);
    }

    public String enterAssetDetailsToUpdate(String categoryDetails) {

        switch (categoryDetails) {
            case "kalyanaMandapam":
                clickOnButton(amentiesAddButton, webDriver);
                enterText(amenties.get(4), "Rooms", webDriver);
                enterText(amenties.get(5), "20", webDriver);
                clickOnButton(amentiesAddButton, webDriver);
                enterText(amenties.get(6), "Parking Floors", webDriver);
                enterText(amenties.get(7), "2", webDriver);
                break;

            case "shopping":
                clickOnButton(amentiesAddButton, webDriver);
                enterText(amenties.get(2), "2", webDriver);
                enterText(amenties.get(3), "5", webDriver);
                break;
        }

        selectFromDropDown(statusSelectBox, "CAPITALIZED", webDriver);
        enterText(webDriver.findElement(By.id("grossValue")), "10000", webDriver);
        enterText(webDriver.findElement(By.id("accumulatedDepreciation")), "10000", webDriver);

        clickOnButton(searchOrUpdateButton, webDriver);
        switchToNewlyOpenedWindow(webDriver);

        String updatedMessage = getTextFromWeb(webDriver.findElements(By.cssSelector("b[style='font-weight: bold;']")).get(0), webDriver);
        clickOnButton(closeButton, webDriver);
        return updatedMessage;
    }

    public void closeAssetViewPage() {
        for (String winHandle : webDriver.getWindowHandles()) {
            String title = webDriver.switchTo().window(winHandle).getCurrentUrl();
            if (title.equals("http://kurnool-pilot-services.egovernments.org/services/asset-web/app/asset/search-asset.html?type=update")) {
                break;
            }
        }
        clickOnButton(closeButton, webDriver);
        switchToPreviouslyOpenedWindow(webDriver);
    }
}
