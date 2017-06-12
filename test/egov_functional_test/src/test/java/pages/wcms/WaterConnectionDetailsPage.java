package pages.wcms;

import entities.wcms.ConnectionInfo;
import entities.wcms.EnclosedDocument;
import entities.wcms.FieldInspectionDetails;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

public class WaterConnectionDetailsPage extends WaterChargeManagementPage {

    @FindBy(id = "waterSource")
    private WebElement waterSourceTypeSelectBox;

    @FindBy(id = "connectionType")
    private WebElement connectionTypeSelectBox;

    @FindBy(id = "propertyType")
    private WebElement propertyTypeSelectBox;

    @FindBy(id = "connectionCategorie")
    private WebElement categorySelectBox;

    @FindBy(id = "usageType")
    private WebElement usageTypeSelectBox;

    @FindBy(id = "pipeSize")
    private WebElement hscPipeSizeSelectBox;

    @FindBy(id = "sumpCapacity")
    private WebElement sumpCapacityTextBox;

    @FindBy(id = "numberOfPerson")
    private WebElement noOfPersonsTextBox;

    @FindBy(id = "connectionReason")
    private WebElement reasonForNewConnection;

    @FindBy(id = "applicationDocs0documentNumber")
    private WebElement documentNo1TextBox;

    @FindBy(id = "applicationDocs0documentDate")
    private WebElement documentDate1TextBox;

    @FindBy(id = "applicationDocs1documentNumber")
    private WebElement documentNo2TextBox;

    @FindBy(id = "applicationDocs1documentDate")
    private WebElement documentDate2TextBox;

    @FindBy(id = "applicationDocs3documentNumber")
    private WebElement documentNo3TextBox;

    @FindBy(id = "applicationDocs3documentDate")
    private WebElement documentDate3TextBox;

    @FindBy(id = "file0id")
    private WebElement browse1Button;

    @FindBy(id = "file1id")
    private WebElement browse2Button;

    @FindBy(id = "file3id")
    private WebElement browse3Button;

    @FindBy(id = "estimationDetails0itemDescription")
    private WebElement fieldInspectionMaterial;

    @FindBy(id = "estimationDetails0quantity")
    private WebElement fieldInspectionQuantity;

    @FindBy(id = "estimationDetails0unitOfMeasurement")
    private WebElement fieldInspectionMeasureUnit;

    @FindBy(id = "estimationDetails0unitRate")
    private WebElement fieldInspectionRate;

    @FindBy(id = "existingPipeline")
    private WebElement fieldInspectionExistingPipeline;

    @FindBy(id = "pipelineDistance")
    private WebElement fieldInspectionPipelineDistance;

    @FindBy(id = "estimationCharges")
    private WebElement fieldInspectionEstimationCharges;

    @FindBy(id = "Submit")
    private WebElement fieldInspectionSubmitButton;

    @FindBy(id = "temporary")
    private WebElement temporaryRadioButton;

    @FindBy(id = "permanent")
    private WebElement permanentRadioButton;

    @FindBy(id = "closeconnectionreason")
    private WebElement closureConnectionReason;

    @FindBy(id = "waterSupplyType")
    private WebElement waterSupplyType;

    @FindBy(id = "buildingName")
    private WebElement apartmentName;

    public WaterConnectionDetailsPage(WebDriver webDriver) {
        super(webDriver);
    }

    public void enterConnectionInfo(ConnectionInfo connectionInfo) {

        selectFromDropDown(connectionTypeSelectBox, connectionInfo.getConnectionType(), webDriver);
        selectFromDropDown(waterSourceTypeSelectBox, connectionInfo.getWaterSourceType(), webDriver);
        selectFromDropDown(propertyTypeSelectBox, connectionInfo.getPropertyType(), webDriver);
        clickOnButton(webDriver.findElement(By.id("connectionCategorie")), webDriver);
        selectFromDropDown(webDriver.findElement(By.id("connectionCategorie")), connectionInfo.getCategory(), webDriver);
        clickOnButton(usageTypeSelectBox , webDriver);
        selectFromDropDown(usageTypeSelectBox, connectionInfo.getUsageType(), webDriver);
        selectFromDropDown(hscPipeSizeSelectBox, connectionInfo.getHscPipeSize(), webDriver);

        enterText(sumpCapacityTextBox, connectionInfo.getSumpCapacity(), webDriver);
        enterText(noOfPersonsTextBox, connectionInfo.getNoOfPersons(), webDriver);

        checkDropdownIsLoadedOrNot(webDriver.findElement(By.id("connectionCategorie")),
                connectionInfo.getCategory() , webDriver , "Select from below");

//        if (webDriver.findElement(By.id("connectionCategorie")).getText().contains("Select from below")) {
//            selectFromDropDown(webDriver.findElement(By.id("connectionCategorie")), connectionInfo.getCategory(), webDriver);
//        }
        checkDropdownIsLoadedOrNot(webDriver.findElement(By.id("usageType")),
                connectionInfo.getUsageType() , webDriver , "Select from below");
//        if (webDriver.findElement(By.id("usageType")).getText().contains("Select from below")) {
//            selectFromDropDown(webDriver.findElement(By.id("usageType")), connectionInfo.getUsageType(), webDriver);
//        }
    }

    public void enterDocumentInfo(EnclosedDocument enclosedDocument) {

        enterText(documentNo1TextBox, enclosedDocument.getDocumentN01(), webDriver);
        enterText(documentNo2TextBox, enclosedDocument.getDocumentN02(), webDriver);
        enterText(documentNo3TextBox, enclosedDocument.getDocumentN03(), webDriver);

        enterDate(documentDate1TextBox, getCurrentDate(), webDriver);
        enterDate(documentDate2TextBox, getCurrentDate(), webDriver);
        enterDate(documentDate3TextBox, getCurrentDate(), webDriver);

        uploadFile(browse1Button, System.getProperty("user.dir") + "/src/test/resources/dataFiles/PTISTestData.xlsx", webDriver);
        uploadFile(browse2Button, System.getProperty("user.dir") + "/src/test/resources/dataFiles/PTISTestData.xlsx", webDriver);
        uploadFile(browse3Button, System.getProperty("user.dir") + "/src/test/resources/dataFiles/PTISTestData.xlsx", webDriver);
    }

    public void enterAdditionalWaterConnectionInfo(ConnectionInfo connectionInfo) {

        selectFromDropDown(waterSourceTypeSelectBox, connectionInfo.getWaterSourceType(), webDriver);
        selectFromDropDown(connectionTypeSelectBox, connectionInfo.getConnectionType(), webDriver);
        selectFromDropDown(propertyTypeSelectBox, connectionInfo.getPropertyType(), webDriver);
        selectFromDropDown(categorySelectBox, connectionInfo.getCategory(), webDriver);
        selectFromDropDown(usageTypeSelectBox, connectionInfo.getUsageType(), webDriver);
        selectFromDropDown(hscPipeSizeSelectBox, connectionInfo.getHscPipeSize(), webDriver);

        enterText(sumpCapacityTextBox, connectionInfo.getSumpCapacity(), webDriver);
        enterText(noOfPersonsTextBox, connectionInfo.getNoOfPersons(), webDriver);
        enterText(reasonForNewConnection, connectionInfo.getReasonForAdditionalConnection(), webDriver);
    }

    public void enterFieldInspectionInfo(FieldInspectionDetails fieldInspectionDetails) {

        enterText(fieldInspectionMaterial, fieldInspectionDetails.getMaterial(), webDriver);
        enterText(fieldInspectionQuantity, fieldInspectionDetails.getQuantity(), webDriver);
        enterText(fieldInspectionMeasureUnit, fieldInspectionDetails.getUnitOfMeasurement(), webDriver);
        enterText(fieldInspectionRate, fieldInspectionDetails.getRate(), webDriver);
        enterText(fieldInspectionExistingPipeline, fieldInspectionDetails.getExistingDistributionPipeline(), webDriver);
        enterText(fieldInspectionPipelineDistance, fieldInspectionDetails.getPipelineToHomeDistance(), webDriver);

        clickOnButton(fieldInspectionSubmitButton, webDriver);
        switchToNewlyOpenedWindow(webDriver);
    }

    public void changeOfUseConnectionInfo(ConnectionInfo connectionInfo) {
        new Select(propertyTypeSelectBox).selectByVisibleText(connectionInfo.getPropertyType());
        for (int i = 0; i <= 10; i++) {
            if (!webDriver.findElement(By.id("pipeSize")).getText().equalsIgnoreCase(connectionInfo.getHscPipeSize())) {
                try {
                    waitForElementToBeVisible(hscPipeSizeSelectBox, webDriver);
                    new Select(hscPipeSizeSelectBox).selectByVisibleText(connectionInfo.getHscPipeSize());
                } catch (StaleElementReferenceException e) {
                    WebElement element = webDriver.findElement(By.id("pipeSize"));
                    waitForElementToBeVisible(element, webDriver);
                    new Select(element).selectByVisibleText(connectionInfo.getHscPipeSize());
                }
            }
        }
        enterText(sumpCapacityTextBox, connectionInfo.getSumpCapacity(), webDriver);
        enterText(noOfPersonsTextBox, connectionInfo.getNoOfPersons(), webDriver);

        selectFromDropDown(usageTypeSelectBox, connectionInfo.getUsageType(), webDriver);
        enterText(reasonForNewConnection, connectionInfo.getReasonForAdditionalConnection(), webDriver);
    }

    public String enterDetailsOfClosureConnection(String closureType) {

        WebElement acknowledgementNumber = webDriver.findElement(By.id("applicationNumber"));
        String number = acknowledgementNumber.getText();

        if (closureType.equalsIgnoreCase("Temporary")) {
            jsClick(temporaryRadioButton, webDriver);
        } else {
            jsClick(permanentRadioButton, webDriver);
        }

        enterText(closureConnectionReason, "Not Required", webDriver);
        return number;
    }

    public void enterConnectionInfoForMetered(ConnectionInfo connectionInfo) {
        selectFromDropDown(waterSourceTypeSelectBox, connectionInfo.getWaterSourceType(), webDriver);
        selectFromDropDown(connectionTypeSelectBox, connectionInfo.getConnectionType(), webDriver);
        selectFromDropDown(propertyTypeSelectBox, connectionInfo.getPropertyType(), webDriver);
        clickOnButton(webDriver.findElement(By.id("connectionCategorie")), webDriver);
        selectFromDropDown(categorySelectBox, connectionInfo.getCategory(), webDriver);
        selectFromDropDown(usageTypeSelectBox, connectionInfo.getUsageType(), webDriver);
        selectFromDropDown(hscPipeSizeSelectBox, connectionInfo.getHscPipeSize(), webDriver);

        enterText(sumpCapacityTextBox, connectionInfo.getSumpCapacity(), webDriver);
        enterText(noOfPersonsTextBox, connectionInfo.getNoOfPersons(), webDriver);
        selectFromDropDown(waterSupplyType, connectionInfo.getWaterSupplyType(), webDriver);
        enterText(apartmentName, connectionInfo.getApartmentName(), webDriver);

    }
}
