package pages.financial;

import entities.financial.FinancialBankToBankDetails;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class BankToBankDetailsPage extends FinancialPage {

    @FindBy(id = "fundId")
    private WebElement fundId;

    @FindBy(id = "vouchermis.departmentid")
    private WebElement voucherDepartment;

    @FindBy(id = "vouchermis.function")
    private WebElement voucherFunction;

    @FindBy(id = "fromBankId")
    private WebElement fromBankId;

    @FindBy(id = "fromAccountNumber")
    private WebElement fromAccountNumber;

    @FindBy(id = "toFundId")
    private WebElement toFundId;

    @FindBy(id = "toBankId")
    private WebElement toBankId;

    @FindBy(id = "toAccountNumber")
    private WebElement toAccountNumber;

    @FindBy(id = "chequeNum")
    private WebElement referenceNumber;

    @FindBy(id = "amount")
    private WebElement amountTextBox;

    @FindBy(id = "Save_New")
    private WebElement saveButton;

    public BankToBankDetailsPage(WebDriver webDriver) {
        super(webDriver);
    }

    public void enterBankToBankDetails(FinancialBankToBankDetails financialBankToBankDetails) {

        selectFromDropDown(fundId, financialBankToBankDetails.getFundId(), webDriver);

        selectFromDropDown(voucherDepartment, financialBankToBankDetails.getVoucherDepartment(), webDriver);

        selectFromDropDown(voucherFunction, financialBankToBankDetails.getVoucherFunction(), webDriver);

        selectFromDropDown(fromBankId, financialBankToBankDetails.getFromBank(), webDriver);

        selectFromDropDown(fromAccountNumber, financialBankToBankDetails.getFromAccountNumber(), webDriver);

        selectFromDropDown(toFundId, financialBankToBankDetails.getToFundId(), webDriver);

        selectFromDropDown(toBankId, financialBankToBankDetails.getToBank(), webDriver);

        selectFromDropDown(toAccountNumber, financialBankToBankDetails.getToAccountNumber(), webDriver);

        enterText(referenceNumber, get6DigitRandomInt(), webDriver);

        enterText(amountTextBox, financialBankToBankDetails.getAmount(), webDriver);

        clickOnButton(saveButton, webDriver);

        switchToNewlyOpenedWindow(webDriver);
    }
}
