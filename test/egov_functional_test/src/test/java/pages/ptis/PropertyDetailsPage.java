package pages.ptis;

import entities.ptis.*;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import pages.BasePage;

import java.util.List;

public class PropertyDetailsPage extends BasePage {

    private WebDriver webDriver;

    @FindBy(id = "propTypeCategoryId")
    private WebElement propertyTypeSelection;

    @FindBy(id = "upicNo")
    private WebElement propertyAssessmentNo;

    @FindBy(id = "propTypeId")
    private WebElement categoryOfOwnershipSelection;

    @FindBy(id = "aadharNo")
    private WebElement aadharNoTextBox;

    @FindBy(id = "mobileNumber")
    private WebElement newMobileNumberTextBox;

    @FindBy(id = "mobileNumber")
    private WebElement mobileNumberTextBox;

    @FindBy(id = "ownerName")
    private WebElement ownerNameTextBox;

    @FindBy(id = "gender")
    private WebElement genderSelection;

    @FindBy(id = "emailId")
    private WebElement emailIdTextBox;

    @FindBy(name = "basicProperty.propertyOwnerInfoProxy[0].owner.guardianRelation")
    private WebElement guardianRelationSelection;

    @FindBy(id = "guardian")
    private WebElement guardianTextBox;

    @FindBy(id = "locality")
    private WebElement localitySelection;

    @FindBy(id = "zoneId")
    private WebElement zoneNumberSelection;

    @FindBy(id = "wardId")
    private WebElement wardNumberSelection;

    @FindBy(id = "blockId")
    private WebElement blockNumberSelection;

    @FindBy(id = "electionWardId")
    private WebElement electionWardSeletion;

    @FindBy(id = "createProperty-create_pinCode")
    private WebElement pincodeTextBox;

    @FindBy(id = "createProperty-create_houseNumber")
    private WebElement doorNumberTextBox;

    @FindBy(id = "mutationId")
    private WebElement reasonForCreationSelection;

    @FindBy(id = "areaOfPlot")
    private WebElement extentOfSiteTextBox;

    @FindBy(id = "propertyDetail.occupancyCertificationNo")
    private WebElement occupancyCertificateNumberTextBox;

    @FindBy(id = "assessmentDocumentNames")
    private WebElement documentTypeDropBox;

    @FindBy(id = "docNo")
    private WebElement deedNoTextBox;

    @FindBy(id = "docDate")
    private WebElement DeedDateDateBox;

    @FindBy(id = "propertyDetail.lift")
    private WebElement liftCheckbox;

    @FindBy(id = "propertyDetail.toilets")
    private WebElement toiletsCheckbox;

    @FindBy(id = "propertyDetail.waterTap")
    private WebElement waterTapCheckbox;

    @FindBy(id = "propertyDetail.electricity")
    private WebElement electricityCheckbox;

    @FindBy(id = "propertyDetail.attachedBathRoom")
    private WebElement attachedBathroomCheckbox;

    @FindBy(id = "propertyDetail.waterHarvesting")
    private WebElement waterHarvestingCheckbox;

    @FindBy(id = "propertyDetail.cable")
    private WebElement cableConnectionCheckbox;

    @FindBy(id = "floorTypeId")
    private WebElement floorTypeSelection;

    @FindBy(id = "roofTypeId")
    private WebElement roofTypeSelection;

    @FindBy(id = "wallTypeId")
    private WebElement wallTypeSelection;

    @FindBy(id = "woodTypeId")
    private WebElement woodTypeSelection;

    @FindBy(id = "floorNo")
    private WebElement floorNumberSelection;

    @FindBy(id = "floorConstType")
    private WebElement classificationOfBuildingSelection;

    @FindBy(id = "floorUsage")
    private WebElement natureOfUsageSelection;

    @FindBy(id = "firmName")
    private WebElement firmNameTextBox;

    @FindBy(id = "floorOccupation")
    private WebElement occupancySelection;

    @FindBy(id = "occupantName")
    private WebElement occupantNameTextBox;

    @FindBy(id = "propertyDetail.floorDetailsProxy[0].constructionDate")
    private WebElement constructionDateTextBox;

    @FindBy(id = "propertyDetail.floorDetailsProxy[0].occupancyDate")
    private WebElement effectiveFromDateTextBox;

    @FindBy(id = "unstructuredLand")
    private WebElement unstructuredLandSelection;

    @FindBy(id = "builtUpArealength")
    private WebElement lengthTextBox;

    @FindBy(id = "builtUpAreabreadth")
    private WebElement breadthTextBox;

    @FindBy(id = "builtUpArea")
    private WebElement plinthAreaTextBox;

    @FindBy(id = "propertyDetail.floorDetailsProxy[0].buildingPermissionNo")
    private WebElement buildingPermissionNumberTextBox;

    @FindBy(id = "propertyDetail.floorDetailsProxy[0].buildingPermissionDate")
    private WebElement buildingPermissionDateTextBox;

    @FindBy(id = "propertyDetail.floorDetailsProxy[0].buildingPlanPlinthArea.area")
    private WebElement plinthAreaInBuildingPlanTextBox;

    @FindBy(css = "input[id='Forward'][type='submit']")
    private WebElement forwardButton;

    @FindBy(id = "Approve")
    private WebElement approveButton;

    @FindBy(id = "Sign")
    private WebElement signButton;

    @FindBy(id = "Generate Notice")
    private WebElement generateNotice;

    @FindBy(id = "Create")
    private WebElement create;

    @FindBy(id = "propertyIdentifier")
    private WebElement assessmentNumberTextBox1;

    @FindBy(id = "consumerCodeData")
    private WebElement hscNumberTextBox;

    @FindBy(id = "executionDate")
    private WebElement connectionDateTextBox;

    @FindBy(xpath = ".//*[@id='assessmentNum']")
    private WebElement assessmentNumTextBox;

    @FindBy(id = "searchByassmentno")
    private WebElement searchButtonByAssmentNo;

    @FindBy(id = "doorNo")
    private WebElement doorNoTextBox;

    @FindBy(css = "input[id='zoneform_houseNumBndry'][type='text']")
    private WebElement houseNumTextBoxForSearch;

    @FindBy(css = "input[id='zoneform_ownerNameBndry'][type='text']")
    private WebElement ownerNameTextBoxForSearch;

    @FindBy(id = "mobileNumber")
    private WebElement mobileNoTextBox;

    @FindBy(id = "searchDoorno")
    private WebElement searchButtonByDoorNo;

    @FindBy(id = "searchMobileno")
    private WebElement searchButtonByMobileNo;

    @FindBy(id = "zoneId")
    private WebElement zoneId;

    @FindBy(id = "wardId")
    private WebElement wardId;

    @FindBy(id = "searchByBndry")
    private WebElement searchButtonByZoneAndWard;

    @FindBy(id = "Create")
    private WebElement submitButton;

    @FindBy(id = "upicNo")
    private WebElement assessmentNumberTextBox;

    @FindBy(id = "Create")
    private WebElement createButton;

    @FindBy(id = "assessmentNum")
    private WebElement assessmentTextbox;

    @FindBy(id = "assessmentform_search")
    private WebElement searchButton;

    @FindBy(name = "assessmentNum")
    private WebElement searchAssessmentTextBox;

    @FindBy(id = "certificationNumber")
    private WebElement editOccupancyTextBox;

    @FindBy(id = "occupantname")
    private WebElement editoccupantNameTextBox;

    @FindBy(id = "propertyDetail.floorDetailsProxy[%#floorsstatus.index].constructionDate")
    private WebElement editconstructionDateTextBox;

    @FindBy(id = "propertyDetail.floorDetailsProxy[%#floorsstatus.index].occupancyDate")
    private WebElement editeffectiveFromDateTextBox;

    @FindBy(id = "approvalComent")
    private WebElement approvalWaterComment;

    @FindBy(id = "Forward")
    private WebElement additionalForwardButton;

    @FindBy(css = "input[value='Pay'][type='submit']")
    private WebElement payButton;

    @FindBy(css = "input[type='text'][name = 'totalamounttobepaid']")
    private WebElement propertyAmountPaid;

    @FindBy(css = "input[type='text'][name='instrHeaderCash.instrumentAmount']")
    private WebElement propertyAmountToBePaid;

    @FindBy(xpath = ".//*[@id='approve']/div/table/tbody/tr[1]/td/span[2]")
    private WebElement commAssessmentNo1;

    @FindBy(id = "locationId")
    private WebElement locationBoxForSearch;

    @FindBy(id = "ownerName")
    private WebElement ownerNameBoxForSearch;

    @FindBy(id = "searchByowner")
    private WebElement searchButtonForOwner;

    @FindBy(id = "fromDemand")
    private WebElement fromTextBox;

    @FindBy(id = "toDemand")
    private WebElement toTextBox;

    @FindBy(id = "searchByDemand")
    private WebElement searchButtonForDemand;

    @FindBy(id = "fromDate")
    private WebElement vacancyFromDate;

    @FindBy(id = "toDate")
    private WebElement vacancyToDate;

    @FindBy(id = "vacancyComments")
    private WebElement vacancyComments;

    @FindBy(id = "assessmentNo")
    private WebElement AmalgamatedPropertiesTextBox;

    @FindBy(id = "parentIndex")
    private WebElement assessmentNumberOfParentPropertyTextBox;

    @FindBy(id = "assessmentNum")
    private WebElement bifurcationAssessmentNo;

    @FindBy(name = "propertyDetail.floorDetailsProxy[0].constructionDate")
    private WebElement constructionDate;

    @FindBy(id = "floorDetailsEntered")
    private WebElement floorDetailsEnteredCheckBox;

    @FindBy(id = "createProperty-create_documents_4__uploads")
    private WebElement PhotoOfAssessment;

    @FindBy(id = "applicationCheck")
    private WebElement applicationCheckBox;

    public PropertyDetailsPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void enterPropertyHeader(PropertyHeaderDetails propertyHeaderDetails) {
        selectFromDropDown(categoryOfOwnershipSelection, propertyHeaderDetails.getCategoryOfOwnership(), webDriver);
        maximizeBrowserWindow(webDriver);
        selectFromDropDown(propertyTypeSelection, propertyHeaderDetails.getPropertyType(), webDriver);
    }

    public void enterOwnerDetails(OwnerDetails ownerDetails) {
        enterText(newMobileNumberTextBox, ownerDetails.getMobileNumber(), webDriver);
        mobileNumberTextBox.sendKeys("94488" + get6DigitRandomInt());
        enterText(ownerNameTextBox, ownerDetails.getOwnerName(), webDriver);
        selectFromDropDown(genderSelection, ownerDetails.getGender().toUpperCase(), webDriver);
        JavascriptExecutor executor = (JavascriptExecutor) webDriver;
        executor.executeScript(String.format("document.getElementById('emailId').setAttribute('value', '%s')", ownerDetails.getEmailAddress()));
        selectFromDropDown(guardianRelationSelection, ownerDetails.getGuardianRelation(), webDriver);
        enterText(guardianTextBox, ownerDetails.getGuardianName(), webDriver);
    }

    public void enterPropertyAddressDetails(PropertyAddressDetails addressDetails) {
        selectFromDropDown(localitySelection, addressDetails.getLocality(), webDriver);
        selectFromDropDown(zoneNumberSelection, addressDetails.getZoneNumber(), webDriver);
        selectFromDropDown(electionWardSeletion, addressDetails.getElectionWard(), webDriver);
        enterText(doorNumberTextBox,"#"+get6DigitRandomInt(), webDriver);
        enterText(pincodeTextBox, addressDetails.getPincode(), webDriver);
    }

    public void enterAssessmentDetails(AssessmentDetails assessmentDetails) {
        selectFromDropDown(reasonForCreationSelection, assessmentDetails.getReasonForCreation(), webDriver);
        enterText(extentOfSiteTextBox, assessmentDetails.getExtentOfSite(), webDriver);
        enterText(occupancyCertificateNumberTextBox, assessmentDetails.getOccupancyCertificateNumber(), webDriver);
    }

    public void selectAmenities(Amenities amenities) {
        selectAmenityIfRequired(liftCheckbox, amenities.getLift());
        selectAmenityIfRequired(toiletsCheckbox, amenities.getToilets());
        selectAmenityIfRequired(attachedBathroomCheckbox, amenities.getAttachedBathroom());
        selectAmenityIfRequired(electricityCheckbox, amenities.getElectricity());
        selectAmenityIfRequired(waterTapCheckbox, amenities.getWaterTap());
        selectAmenityIfRequired(waterHarvestingCheckbox, amenities.getWaterHarvesting());
        selectAmenityIfRequired(cableConnectionCheckbox, amenities.getCableConnection());
    }

    private void selectAmenityIfRequired(WebElement element, Boolean hasAmenity) {
        if (hasAmenity && !element.isSelected())
            element.click();
    }

    public void enterConstructionTypeDetails(ConstructionTypeDetails constructionTypeDetails) {
        selectFromDropDown(floorTypeSelection, constructionTypeDetails.getFloorType(), webDriver);
        selectFromDropDown(roofTypeSelection, constructionTypeDetails.getRoofType(), webDriver);
        selectFromDropDown(woodTypeSelection, constructionTypeDetails.getWoodType(), webDriver);
        selectFromDropDown(wallTypeSelection, constructionTypeDetails.getWallType(), webDriver);
    }

    public void enterFloorDetails(FloorDetails floorDetails) {
        selectFromDropDown(floorNumberSelection, floorDetails.getFloorNumber(), webDriver);
        selectFromDropDown(classificationOfBuildingSelection, floorDetails.getClassificationOfBuilding(), webDriver);
        selectFromDropDown(natureOfUsageSelection, floorDetails.getNatureOfUsage(), webDriver);
//      enterText(firmNameTextBox, floorDetails.getFirmName(), webDriver);
        selectFromDropDown(occupancySelection, floorDetails.getOccupancy(), webDriver);
        enterText(occupantNameTextBox, floorDetails.getOccupantName(), webDriver);
        enterDate(constructionDateTextBox, floorDetails.getConstructionDate(), webDriver);
        constructionDateTextBox.sendKeys(Keys.TAB);
        enterDate(effectiveFromDateTextBox, floorDetails.getEffectiveFromDate(), webDriver);
        effectiveFromDateTextBox.sendKeys(Keys.TAB);
        selectFromDropDown(unstructuredLandSelection, floorDetails.getUnstructuredLand(), webDriver);
        enterText(lengthTextBox, floorDetails.getLength(), webDriver);
        enterText(breadthTextBox, floorDetails.getBreadth(), webDriver);
        enterText(buildingPermissionNumberTextBox, floorDetails.getBuildingPermissionNumber(), webDriver);
        enterDate(buildingPermissionDateTextBox, floorDetails.getBuildingPermissionDate(), webDriver);
        enterText(plinthAreaInBuildingPlanTextBox, floorDetails.getPlinthAreaInBuildingPlan(), webDriver);
    }

    public void clickOnFloorDetailsCheckBox() {
        jsClickCheckbox(floorDetailsEnteredCheckBox, webDriver);
    }

    public void selectDocumentType(DocumentTypeValue documentValue) {
        selectFromDropDown(documentTypeDropBox, documentValue.getDocumentType(), webDriver);
        enterText(deedNoTextBox, documentValue.getDeedNo(), webDriver);
        enterDate(DeedDateDateBox, documentValue.getDeedDate(), webDriver);
        uploadFile(PhotoOfAssessment, System.getProperty("user.dir") + "/src/test/resources/dataFiles/PTISTestData.xlsx", webDriver);
    }

    public void forward() {
        clickOnButton(applicationCheckBox,webDriver);
        if(forwardButton.isEnabled()) {
            clickOnButton(forwardButton, webDriver);
        }else {
            clickOnButton(applicationCheckBox,webDriver);
            clickOnButton(forwardButton, webDriver);
        }
    }

    public void approve() {
        clickOnButton(approveButton, webDriver);
    }

    public String approveForCreation() {
        clickOnButton(approveButton, webDriver);
        waitForElementToBeVisible(commAssessmentNo1, webDriver);
        return commAssessmentNo1.getText();
    }

    public void digitallySign() {
        clickOnButton(signButton, webDriver);
    }

    public void generateNotice() {
        clickOnButton(generateNotice, webDriver);
        switchToNewlyOpenedWindow(webDriver);
        webDriver.close();
        switchToPreviouslyOpenedWindow(webDriver);
    }

    public void checkNoOfRecords() {
        Boolean isPresent = webDriver.findElements(By.id("currentRowObject")).size() > 0;
        if (isPresent) {
            WebElement tableId = webDriver.findElement(By.id("currentRowObject"));
            waitForElementToBeVisible(tableId, webDriver);
            List<WebElement> totalRows = tableId.findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
            System.out.println(" total Number of Records of:" + totalRows.size() + "\n");
        } else {
            System.out.println(" no records\n");
        }
    }

    public void chooseToSubmit() {
        clickOnButton(submitButton, webDriver);
    }

    public void enterAssessmentNumber(String assessmentNumber) {
        enterText(assessmentNumberTextBox, assessmentNumber, webDriver);
    }

    public void create() {
        jsClick(createButton, webDriver);
    }

    public void searchAssessmentNumber(String assessmentNum) {
        enterText(searchAssessmentTextBox, assessmentNum, webDriver);
    }

    public void search() {
        clickOnButton(searchButton, webDriver);
    }

    public void enterEditAssessmentDetails(EditAssessmentDetails assessmentDetails) {
        enterText(extentOfSiteTextBox, assessmentDetails.getExtentOfSite(), webDriver);
        enterText(editOccupancyTextBox, assessmentDetails.getOccupancyCertificateNumber(), webDriver);
    }

    public void enterEditFloorDetails(EditFloorDetails floorDetails) {
        selectFromDropDown(floorNumberSelection, floorDetails.getEditfloorNumber(), webDriver);
        selectFromDropDown(classificationOfBuildingSelection, floorDetails.getEditclassificationOfBuilding(), webDriver);
        selectFromDropDown(natureOfUsageSelection, floorDetails.getEditnatureOfUsage(), webDriver);
        selectFromDropDown(occupancySelection, floorDetails.getEditoccupancy(), webDriver);
        enterText(editoccupantNameTextBox, floorDetails.getEditoccupantName(), webDriver);
        enterDate(editconstructionDateTextBox, floorDetails.getEditconstructionDate(), webDriver);
        enterDate(editeffectiveFromDateTextBox, floorDetails.getEditeffectiveFromDate(), webDriver);
        selectFromDropDown(unstructuredLandSelection, floorDetails.getEditunstructuredLand(), webDriver);
        enterText(lengthTextBox, floorDetails.getEditlength(), webDriver);
        enterText(breadthTextBox, floorDetails.getEditbreadth(), webDriver);
        enterText(buildingPermissionNumberTextBox, floorDetails.getEditbuildingPermissionNumber(), webDriver);
        enterDate(buildingPermissionDateTextBox, floorDetails.getEditbuildingPermissionDate(), webDriver);
        enterText(plinthAreaInBuildingPlanTextBox, floorDetails.getEditplinthAreaInBuildingPlan(), webDriver);
    }

    public void payCash() {
        waitForElementToBeClickable(propertyAmountPaid, webDriver);
        waitForElementToBeClickable(propertyAmountToBePaid, webDriver);
        propertyAmountToBePaid.sendKeys(propertyAmountPaid.getAttribute("value").split("\\.")[0]);
        WebElement element = webDriver.findElement(By.id("button2"));
        JavascriptExecutor executor = (JavascriptExecutor) webDriver;
        executor.executeScript("arguments[0].click();", element);
        switchToNewlyOpenedWindow(webDriver);
    }

    public void searchProperty(SearchDetails searchDetails, String searchType) {
        System.out.println(searchType + " has ");
        switch (searchType) {

            case "searchWithAssessmentNumber":
                enterText(assessmentNumTextBox, searchDetails.getSearchValue1(), webDriver);
                clickOnButton(searchButtonByAssmentNo, webDriver);
                break;

            case "searchWithMobileNumber":
                enterText(mobileNoTextBox, searchDetails.getSearchValue1(), webDriver);
                clickOnButton(searchButtonByMobileNo, webDriver);
                break;

            case "searchWithDoorNumber":
                enterText(doorNoTextBox, searchDetails.getSearchValue1(), webDriver);
                clickOnButton(searchButtonByDoorNo, webDriver);
                break;

            case "searchWithZoneAndWardNumber":
                selectFromDropDown(zoneId, searchDetails.getSearchValue1(), webDriver);
                selectFromDropDown(wardId, searchDetails.getSearchValue2(), webDriver);
                enterText(houseNumTextBoxForSearch, searchDetails.getSearchValue3(), webDriver);
                enterText(ownerNameTextBoxForSearch, searchDetails.getSearchValue4(), webDriver);
                clickOnButton(searchButtonByZoneAndWard, webDriver);
                break;

            case "searchWithOwnerName":
                selectFromDropDown(locationBoxForSearch, searchDetails.getSearchValue3(), webDriver);
                enterText(ownerNameBoxForSearch, searchDetails.getSearchValue4(), webDriver);
                clickOnButton(searchButtonForOwner, webDriver);
                break;

            case "searchByDemand":
                enterText(fromTextBox, searchDetails.getSearchValue3(), webDriver);
                enterText(toTextBox, searchDetails.getSearchValue4(), webDriver);
                clickOnButton(searchButtonForDemand, webDriver);
                break;
        }
    }

    public void approveaddition() {
        clickOnButton(approveButton, webDriver);
    }

    public void enterVacancyRemissionDetails() {
        WebElement vacancyRemissionDoc1 = webDriver.findElement(By.id("file0"));
        WebElement vacancyRemissionDoc2 = webDriver.findElement(By.id("file1"));
        uploadFile(vacancyRemissionDoc1, System.getProperty("user.dir") + "/src/test/resources/dataFiles/PTISTestData.xlsx", webDriver);
        uploadFile(vacancyRemissionDoc2, System.getProperty("user.dir") + "/src/test/resources/dataFiles/PTISTestData.xlsx", webDriver);
        enterDate(vacancyFromDate, getCurrentDate(), webDriver);
        enterDate(vacancyToDate, getFutureDate(184), webDriver);
        enterText(vacancyComments, "Vacancy Remmission", webDriver);
    }

    public void searchAmalgamatedProperties() {
        enterText(AmalgamatedPropertiesTextBox, "1016071673", webDriver);
        AmalgamatedPropertiesTextBox.sendKeys(Keys.TAB);
        enterDate(constructionDate, getPastDate(184), webDriver);
    }

    public void enterBifurcationDetails(AssessmentDetails bifurcationDetails,String num) {
        selectFromDropDown(reasonForCreationSelection, bifurcationDetails.getBifurcationReasonForCreation(), webDriver);
        enterText(assessmentNumberOfParentPropertyTextBox,num, webDriver);
        enterText(extentOfSiteTextBox, bifurcationDetails.getExtentOfSite(), webDriver);
        enterText(occupancyCertificateNumberTextBox, bifurcationDetails.getOccupancyCertificateNumber(), webDriver);

    }

    public void enterBifurcationAssessmentNo(String number) {
        enterText(bifurcationAssessmentNo, number, webDriver);
        clickOnButton(searchButton, webDriver);
    }


    public void enterExtentOfSiteValue() {
        enterText(extentOfSiteTextBox,"200",webDriver);
        uploadFile(PhotoOfAssessment, System.getProperty("user.dir") + "/src/test/resources/dataFiles/PTISTestData.xlsx", webDriver);
    }

    public void checkValidationForOwner() {
        validationCheck(mobileNumberTextBox,"kakaka","9444"+get6DigitRandomInt());
        validationCheck(ownerNameTextBox,"2222","Bimal");
        validationCheck(ownerNameTextBox,"@@@","Bimal");
        selectFromDropDown(genderSelection,"MALE",webDriver);
        validationCheck(emailIdTextBox,"kakaka","testing@gmail.com");
        selectFromDropDown(guardianRelationSelection,"Father",webDriver);
        validationCheck(guardianTextBox,"2222","Ram");
        validationCheck(guardianTextBox,"@@@","Ram");
    }

    public void checkValidationForAssessmentDetails(){
        selectFromDropDown(reasonForCreationSelection,"NEW PROPERTY",webDriver);
        validationCheck(extentOfSiteTextBox,"kakaka","200");
        validationCheck(extentOfSiteTextBox,"@@@","200");
    }

    public void checkValidationForFloorDetails(){
        validationCheck(lengthTextBox,"kakaa","10.0");
        validationCheck(lengthTextBox,"@@@","10.0");
        validationCheck(breadthTextBox,"kakak","20.0");
        validationCheck(breadthTextBox,"@@@","20.0");
        validationCheck(plinthAreaInBuildingPlanTextBox,"kakak","30.0");
        validationCheck(plinthAreaInBuildingPlanTextBox,"@@@","30.0");
    }

    public void checkValidationForLocationDetails(){
        selectFromDropDown(localitySelection, "4th colony", webDriver);
        selectFromDropDown(zoneNumberSelection, "Zone-1", webDriver);
        selectFromDropDown(electionWardSeletion,"Election Ward No. 1", webDriver);
        enterText(doorNumberTextBox,"12/46", webDriver);
        validationCheck(pincodeTextBox,"kakak","540037");
        validationCheck(pincodeTextBox,"@@@","540037");
    }

    public void validationCheck(WebElement element,String wrongData,String correctData){
        enterText(element,wrongData,webDriver);
        element.sendKeys(Keys.TAB);
        WebElement element1 = webDriver.findElement(By.cssSelector("div[class~='bootbox-alert'] button[class^='btn']"));
        clickOnButton(element1,webDriver);
        enterText(element,correctData,webDriver);
    }

    public void checkDoorNumber() {
        enterText(doorNumberTextBox,"#"+get6DigitRandomInt(),webDriver);
    }


}
