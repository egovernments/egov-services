package entities.tradeLicense;

public class TradeOwnerDetails {

    private String aadhaarNumber;
    private String mobileNumber;
    private String tradeOwnerName;
    private String fatherSpouseName;
    private String emailId;
    private String tradeOwnerAddress;

    public String getAadhaarNumber() {
        return aadhaarNumber;
    }

    public void setAadhaarNumber(String aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getTradeOwnerName() {
        return tradeOwnerName;
    }

    public void setTradeOwnerName(String tradeOwnerName) {
        this.tradeOwnerName = tradeOwnerName;
    }

    public String getFatherSpouseName() {
        return fatherSpouseName;
    }

    public void setFatherSpouseName(String fatherSpouseName) {
        this.fatherSpouseName = fatherSpouseName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getTradeOwnerAddress() {
        return tradeOwnerAddress;
    }

    public void setTradeOwnerAddress(String tradeOwnerAddress) {
        this.tradeOwnerAddress = tradeOwnerAddress;
    }
}
