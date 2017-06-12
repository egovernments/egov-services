package pages.ptis;

import entities.ptis.RegistrationDetails;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.BasePage;

import java.util.List;

public class TransferDetailsPage extends BasePage {

    private WebDriver webdriver;
    @FindBy(id = "REGISTERED TRANSFER")
    private WebElement registrationAlreadyDoneButton;
    @FindBy(id = "seller")
    private WebElement sellerExecutantNameTextBox;
    @FindBy(id = "buyer")
    private WebElement buyerClaimantNameTextBox;
    @FindBy(id = "doorNo")
    private WebElement doorNoTextBox;
    @FindBy(id = "address")
    private WebElement propertyAddressTextBox;
    @FindBy(id = "mobileNumber")
    private WebElement transferMobileNumber;
    @FindBy(id = "plotArea")
    private WebElement registeredPlotAreaTextBox;
    @FindBy(id = "plinthArea")
    private WebElement registeredPlinthAreaTextBox;
    @FindBy(id = "eastBoundary")
    private WebElement eastBoundaryTextBox;
    @FindBy(id = "westBoundary")
    private WebElement westBoundaryTextBox;
    @FindBy(id = "northBoundary")
    private WebElement northBoundaryTextBox;
    @FindBy(id = "southBoundary")
    private WebElement southBoundaryTextBox;
    @FindBy(id = "sroName")
    private WebElement sRONameTextBox;
    @FindBy(id = "transRsnId")
    private WebElement reasonforTransfersection;
    @FindBy(id = "docNum")
    private WebElement registrationDocumentNumberTextBox;
    @FindBy(id = "deedDate")
    private WebElement registrationDocumentDateTextBox;
    @FindBy(id = "partyValue")
    private WebElement partiesConsiderationValueTextBox;
    @FindBy(id = "departmentValue")
    private WebElement departmentGuidelinesValueTextBox;
    @FindBy(name = "assessmentNo")
    private WebElement searchMutationTextBox;
    @FindBy(className = "buttonsubmit")
    private WebElement payFeeButton;
    @FindBy(id = "Generate Title Transfer Notice")
    private WebElement titleTransferNoticeTextBox;
    @FindBy(id = "assessmentNum")
    private WebElement searchAssessmentNumberTextBox;
    @FindBy(id = "taxExemptedReason")
    private WebElement exemptionReasonDropdown;
    @FindBy(xpath = ".//*[@id='propertyAckForm']/div[1]/div/div/strong")
    private WebElement successPageMessage;
    @FindBy(css = "a[class='btn btn-default']")
    private List<WebElement> taxExemptionCloseButton;
    @FindBy(css = "input[type='button'][value='Close']")
    private WebElement closeButton;
    @FindBy(id = "applicationCheck")
    private WebElement declarationCheckBox;
    @FindBy(css = "input[id=check]")
    private WebElement permisesUsedCheckBoxButton;

    public TransferDetailsPage(WebDriver webdriver) {
        this.webdriver = webdriver;
    }

    public void chooseRegistrationAlreadyDone() {
        waitForElementToBeClickable(registrationAlreadyDoneButton, webdriver);
        registrationAlreadyDoneButton.click();
    }

    public void enterRegistrationDetails(RegistrationDetails registrationDetails) {

        enterText(transferMobileNumber, "2299087661", webdriver);
        enterText(sellerExecutantNameTextBox, registrationDetails.getSellerExecutantName(), webdriver);
        enterText(buyerClaimantNameTextBox, registrationDetails.getBuyerClaimantName(), webdriver);
        enterText(doorNoTextBox, registrationDetails.getDoorNo(), webdriver);
        enterText(propertyAddressTextBox, registrationDetails.getPropertyAddress(), webdriver);
        enterText(registeredPlotAreaTextBox, registrationDetails.getRegisteredPlotArea(), webdriver);
        enterText(registeredPlinthAreaTextBox, registrationDetails.getRegisteredPlinthArea(), webdriver);
        enterText(eastBoundaryTextBox, registrationDetails.getEastBoundary(), webdriver);
        enterText(westBoundaryTextBox, registrationDetails.getWestBoundary(), webdriver);
        enterText(northBoundaryTextBox, registrationDetails.getNorthBoundary(), webdriver);
        enterText(southBoundaryTextBox, registrationDetails.getSouthBoundary(), webdriver);
        enterText(sRONameTextBox, registrationDetails.getSroName(), webdriver);
        selectFromDropDown(reasonforTransfersection, registrationDetails.getReasonForChange(), webdriver);
        enterText(registrationDocumentNumberTextBox, registrationDetails.getRegistrationDocumentNumber(), webdriver);
        enterText(registrationDocumentDateTextBox, registrationDetails.getRegistrationDocumentDate(), webdriver);
        enterText(partiesConsiderationValueTextBox, registrationDetails.getPartiesConsiderationValue(), webdriver);
        enterText(departmentGuidelinesValueTextBox, registrationDetails.getDepartmentGuidelinesValue(), webdriver);
    }

    public void enterEnclosureDetails() {

        WebElement document1 = webdriver.findElement(By.id("save_documents_0__uploads"));
        uploadFile(document1, System.getProperty("user.dir") + "/src/test/resources/dataFiles/PTISTestData.xlsx", webdriver);
        WebElement document2 = webdriver.findElement(By.id("save_documents_1__uploads"));
        uploadFile(document2, System.getProperty("user.dir") + "/src/test/resources/dataFiles/PTISTestData.xlsx", webdriver);
        WebElement document3 = webdriver.findElement(By.id("save_documents_2__uploads"));
        uploadFile(document3, System.getProperty("user.dir") + "/src/test/resources/dataFiles/PTISTestData.xlsx", webdriver);
        WebElement document4 = webdriver.findElement(By.id("save_documents_3__uploads"));
        uploadFile(document4, System.getProperty("user.dir") + "/src/test/resources/dataFiles/PTISTestData.xlsx", webdriver);
    }

    public void searchAssessmentNumber(String mutationAssessmentNumber) {
        enterText(searchMutationTextBox, mutationAssessmentNumber, webdriver);
        clickOnButton(payFeeButton, webdriver);
    }

    public void generateTitleTransferNotice() {
        clickOnButton(titleTransferNoticeTextBox, webdriver);
        switchToNewlyOpenedWindow(webdriver);
        webdriver.close();
        switchToPreviouslyOpenedWindow(webdriver);
    }

    public void selectExemptionReason() {
        selectFromDropDown(exemptionReasonDropdown, "Places set apart for public worship", webdriver);
    }

    public String successMessage() {
        String msg = getTextFromWeb(successPageMessage, webdriver);
        return msg;
    }

    public String getApplicationNumber(String type) {
        WebElement element = webdriver.findElement((By.xpath(".//*[@id='" + type + "']/div[1]/div/div")));
//        System.out.println(element.getText());
        return element.getText();
    }

    public void close() {
        clickOnButton(taxExemptionCloseButton.get(1), webdriver);
        switchToPreviouslyOpenedWindow(webdriver);
    }

    public void closesAcknowledgement() {
        if (webdriver.findElements(By.linkText("Close")).size() > 0) {
            clickOnButton(webdriver.findElement(By.linkText("Close")), webdriver);
        } else {
            clickOnButton(webdriver.findElement(By.id("button2")), webdriver);
        }
        switchToPreviouslyOpenedWindow(webdriver);
    }

    public void selectDeclarationCheckBox() {
        clickOnButton(declarationCheckBox,webdriver);
    }

    public void uploadFiles() {
        for(int i=0;i<2;i++){
            uploadFile(webdriver.findElements(By.id("file"+i)).get(0),System.getProperty("user.dir") + "/src/test/resources/dataFiles/PTISTestData.xlsx",webdriver);
        }
    }

    public void clickOnPremisesCheckBox() {
       clickOnButton(permisesUsedCheckBoxButton,webdriver);
    }
}
