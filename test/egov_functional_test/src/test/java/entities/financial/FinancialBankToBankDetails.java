package entities.financial;

public class FinancialBankToBankDetails {

    private String fundId;
    private String voucherDepartment;
    private String voucherFunction;
    private String fromBank;
    private String fromAccountNumber;
    private String toFundId;
    private String toBank;
    private String toAccountNumber;
    private String amount;

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    public String getVoucherDepartment() {
        return voucherDepartment;
    }

    public void setVoucherDepartment(String voucherDepartment) {
        this.voucherDepartment = voucherDepartment;
    }

    public String getVoucherFunction() {
        return voucherFunction;
    }

    public void setVoucherFunction(String voucherFunction) {
        this.voucherFunction = voucherFunction;
    }

    public String getFromBank() {
        return fromBank;
    }

    public void setFromBank(String fromBank) {
        this.fromBank = fromBank;
    }

    public String getFromAccountNumber() {
        return fromAccountNumber;
    }

    public void setFromAccountNumber(String fromAccountNumber) {
        this.fromAccountNumber = fromAccountNumber;
    }

    public String getToFundId() {
        return toFundId;
    }

    public void setToFundId(String toFundId) {
        this.toFundId = toFundId;
    }

    public String getToBank() {
        return toBank;
    }

    public void setToBank(String toBank) {
        this.toBank = toBank;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }

    public void setToAccountNumber(String toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
