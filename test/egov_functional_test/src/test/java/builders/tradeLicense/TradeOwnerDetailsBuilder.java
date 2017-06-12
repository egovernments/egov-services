package builders.tradeLicense;

import entities.tradeLicense.TradeOwnerDetails;

public class TradeOwnerDetailsBuilder {

    TradeOwnerDetails tradeOwnerDetails = new TradeOwnerDetails();

    public TradeOwnerDetailsBuilder withAadhaarNumber(String aadhaarNumber) {
        tradeOwnerDetails.setAadhaarNumber(aadhaarNumber);
        return this;
    }

    public TradeOwnerDetailsBuilder withMobileNumber(String mobileNumber) {
        tradeOwnerDetails.setMobileNumber(mobileNumber);
        return this;
    }

    public TradeOwnerDetailsBuilder withTradeOwnerName(String tradeOwnerName) {
        tradeOwnerDetails.setTradeOwnerName(tradeOwnerName);
        return this;
    }

    public TradeOwnerDetailsBuilder withFatherSpouseName(String fatherSpouseName) {
        tradeOwnerDetails.setFatherSpouseName(fatherSpouseName);
        return this;
    }

    public TradeOwnerDetailsBuilder withEmailId(String emailId) {
        tradeOwnerDetails.setEmailId(emailId);
        return this;
    }

    public TradeOwnerDetailsBuilder withTradeOwnerAddress(String tradeOwnerAddress) {
        tradeOwnerDetails.setTradeOwnerAddress(tradeOwnerAddress);
        return this;
    }

    public TradeOwnerDetails build() {
        return tradeOwnerDetails;
    }
}


