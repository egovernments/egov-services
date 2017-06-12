package pages.financial;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

import static com.jayway.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;

public class SelectSingleOrMultipleBillsPage extends FinancialPage {

    @FindBy(id = "fromDate")
    private WebElement billFromDate;

    @FindBy(id = "toDate")
    private WebElement billToDate;

    @FindBy(id = "fundId")
    private WebElement fundId;

    @FindBy(id = "searchBtn")
    private WebElement billSearch;

    @FindBy(linkText = "Expense Bill")
    private WebElement expenseBillSearch;

    @FindBy(id = "vouchermis.departmentid")
    private WebElement voucherDepartment;

    @FindBy(id = "vouchermis.function")
    private WebElement voucherFunction;

    @FindBy(id = "expSelectAll")
    private WebElement selectAllBillsFromExpense;

    @FindBy(linkText = "Supplier Bill")
    private WebElement supplierBillSearch;

    @FindBy(xpath = ".//*[@id='supSelectAll']/tbody/tr[2]/td//div/table/tbody/tr/th/input")
    private WebElement selectAllBillsFromSupplier;

    @FindBy(id = "conSelectAll")
    private WebElement selectAllBillsFromContractor;

    @FindBy(id = "generatePayment")
    private WebElement generatePayment;

    @FindBy(id = "isSelected0")
    private WebElement firstBill;

    @FindBy(id = "paymentModecheque")
    private WebElement paymentModeCheque;

    @FindBy(id = "paymentModecash")
    private WebElement paymentModeCash;

    @FindBy(id = "paymentModertgs")
    private WebElement paymentModeRTGS;

    @FindBy(id = "genPayment")
    private WebElement remittancePayment;

    @FindBy(className = "yui-dt-data")
    private WebElement remittanceBillTable;

    private List<WebElement> voucherRows;

    public SelectSingleOrMultipleBillsPage(WebDriver webDriver) {
        super(webDriver);
    }

    public void singleBillSearch() {
        enterDate(billFromDate, getCurrentDate(), webDriver);
        enterDate(billToDate, getCurrentDate(), webDriver);

        selectFromDropDown(fundId, "Municipal Fund", webDriver);
        clickOnButton(billSearch, webDriver);

        switchToNewlyOpenedWindow(webDriver);

        clickOnButton(expenseBillSearch, webDriver);
    }

    public void multipleBillSearch(String type, String paymentMode) {

        selectFromDropDown(fundId, "Municipal Fund", webDriver);
        selectFromDropDown(voucherDepartment, "ADMINISTRATION", webDriver);
        selectFromDropDown(voucherFunction, "General Administration", webDriver);

        clickOnButton(billSearch, webDriver);
        switchToNewlyOpenedWindow(webDriver);

        switch (type) {

            case "expense":
                clickOnButton(expenseBillSearch, webDriver);
                selectAllBillsAtOneTime(selectAllBillsFromExpense);
                break;

            case "supplier":
                clickOnButton(supplierBillSearch, webDriver);
                selectAllBillsAtOneTime(selectAllBillsFromSupplier);
                break;

            case "contractor":
                selectAllBillsAtOneTime(selectAllBillsFromContractor);
                break;
        }

        selectModeOfPayment(paymentMode);
        clickOnButton(generatePayment, webDriver);
        switchToNewlyOpenedWindow(webDriver);
    }

    private void selectAllBillsAtOneTime(WebElement element) {
        if (firstBill.isDisplayed()) {
            clickOnButton(element, webDriver);
        } else {
            throw new RuntimeException("No voucher rows are found in the web page.......All are Successfully Paid -- ");
        }
    }

    private void selectModeOfPayment(String mode) {
        switch (mode) {

            case "cheque":
                jsClick(paymentModeCheque, webDriver);
                break;

            case "cash":
                jsClick(paymentModeCash, webDriver);
                break;

            case "RTGS":
                jsClick(paymentModeRTGS, webDriver);
                break;
        }
    }

    public void actOnAboveVoucher(String paymentMode, String voucherNumber) {

        WebElement table = webDriver.findElement(By.xpath(".//*[@id='cbilltab']/span/table/tbody/tr[2]/td/div/table/tbody"));
        voucherRows = table.findElements(By.tagName("tr"));

        for (WebElement applicationRows : voucherRows.subList(1, voucherRows.size())) {
            if (applicationRows.findElements(By.tagName("td")).get(4).findElements(By.tagName("input")).get(0).getAttribute("value").contains(voucherNumber)) {
                applicationRows.findElements(By.tagName("td")).get(0).findElement(By.cssSelector("input[type='checkbox']")).click();
                break;
            }

        }
        selectModeOfPayment(paymentMode);

        clickOnButton(generatePayment, webDriver);
        switchToNewlyOpenedWindow(webDriver);
    }

    public void selectSingleRemittanceBill(String remittanceBill) {
        int rowNumber = Integer.parseInt(getRemittanceBill(remittanceBill).getText());
        WebElement element = webDriver.findElement(By.id("listRemitBean[" + (rowNumber - 1) + "].chkremit"));
        clickOnButton(element, webDriver);

        clickOnButton(remittancePayment, webDriver);
        switchToNewlyOpenedWindow(webDriver);
    }

    private WebElement getRemittanceBill(String applicationNumber) {

        await().atMost(20, SECONDS).until(() -> remittanceBillTable.findElements(By.tagName("tr")).size() >= 1);
        List<WebElement> applicationRows = remittanceBillTable.findElements(By.tagName("tr"));
        System.out.println("total number of rows -- " + applicationRows.size());
        for (WebElement applicationRow : applicationRows) {
            if (applicationRow.findElements(By.tagName("td")).get(1).
                    findElement(By.className("yui-dt-liner")).findElement(By.tagName("label")).getText().contains(applicationNumber))
                return applicationRow.findElements(By.tagName("td")).get(0).findElement(By.className("yui-dt-liner"));
        }
        throw new RuntimeException("No application row found for -- " + applicationNumber);
    }
}
