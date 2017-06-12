package pages.assetManagement;

import entities.assetManagement.AssetCategoryDetails;
import entities.assetManagement.CustomFieldsDetails;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.BasePage;

public class AssetCategoryPage extends BasePage {

    private WebDriver driver;

    @FindBy(id = "name")
    private WebElement nameTextBox;

    @FindBy(id = "assetCategoryType")
    private WebElement assetCategoryTypeTextBox;

    @FindBy(id = "parent")
    private WebElement parentTypeTextBox;

    @FindBy(id = "depreciationMethod")
    private WebElement depreciationMethodTextBox;

    @FindBy(id = "assetAccount")
    private WebElement assetAccountTextBox;

    @FindBy(id = "accumulatedDepreciationAccount")
    private WebElement accumulatedDepreciationAccountTextBox;

    @FindBy(id = "revaluationReserveAccount")
    private WebElement revaluationReserveAccountTextBox;

    @FindBy(id = "depreciationExpenseAccount")
    private WebElement depreciationExpenseAccountTextBox;

    @FindBy(id = "unitOfMeasurement")
    private WebElement unitOfMeasurementTextBox;

    @FindBy(id = "type")
    private WebElement dataTypeBox;

    @FindBy(id = "regExFormate")
    private WebElement regExFormateTextBox;

    @FindBy(id = "values")
    private WebElement valueTextBox;

    @FindBy(id = "localText")
    private WebElement localTextTextBox;

    @FindBy(id = "isActive")
    private WebElement isActiveCheckBox;

    @FindBy(id = "isMandatory")
    private WebElement isMandatoryCheckBox;

    @FindBy(css = ".glyphicon.glyphicon-plus")
    private WebElement addCustomFieldsButton;

    @FindBy(css = ".btn.btn-primary")
    private WebElement addOrEditButton;

    @FindBy(linkText = "Create")
    private WebElement createButton;

    public AssetCategoryPage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterAssetCategoryDetails(AssetCategoryDetails details) {
        enterText(nameTextBox, details.getName(), driver);
        selectFromDropDown(assetCategoryTypeTextBox, details.getAssetCategoryType(), driver);
        selectFromDropDown(parentTypeTextBox, details.getParentCategory(), driver);
        selectFromDropDown(depreciationMethodTextBox, details.getDepreciationMethod(), driver);
        selectFromDropDown(assetAccountTextBox, details.getAssetAccountCode(), driver);
        selectFromDropDown(accumulatedDepreciationAccountTextBox, details.getAccumulatedDepreciationCode(), driver);
        selectFromDropDown(revaluationReserveAccountTextBox, details.getRevaluationReserveAccountCode(), driver);
        selectFromDropDown(depreciationExpenseAccountTextBox, details.getDepreciationExpenceAccount(), driver);
//        selectFromDropDown(unitOfMeasurementTextBox,details.getUOM(),driver);
    }


    public void enterCustomFieldsDetails(CustomFieldsDetails details) {
        enterText(nameTextBox, details.getName(), driver);
        selectFromDropDown(dataTypeBox, details.getDataType(), driver);
//        enterText(regExFormateTextBox,details.getRegExFormat(),driver);
        if (details.isActive()) {
            clickOnButton(isActiveCheckBox, driver);
        }
        if (details.isMandatory()) {
            clickOnButton(isMandatoryCheckBox, driver);
        }
//        enterText(valueTextBox,details.getValue(),driver);
//        enterText(localTextTextBox,details.getLocalText(),driver);
    }

    public void clickToCreateCustomFields() {
        clickOnButton(addCustomFieldsButton, driver);
    }

    public void addOrEditCustomFieldsButton() {
        clickOnButton(addOrEditButton, driver);
    }

    public void clickOnCreateAssetCategoryButton() {
        clickOnButton(createButton, driver);
    }
}
