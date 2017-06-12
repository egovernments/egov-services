package builders.tradeLicense;

import entities.tradeLicense.TradeDetails;

public class TradeDetailsBuilder {

    TradeDetails tradeDetails = new TradeDetails();

    public TradeDetailsBuilder withtradeTitle(String tradeTitle) {
        tradeDetails.setTradeTitle(tradeTitle);
        return this;
    }

    public TradeDetailsBuilder withtradeType(String tradeType) {
        tradeDetails.setTradeType(tradeType);
        return this;
    }

    public TradeDetailsBuilder withtradeCategory(String tradeCategory) {
        tradeDetails.setTradeCategory(tradeCategory);
        return this;
    }

    public TradeDetailsBuilder withtradeSubCategory(String tradeSubCategory) {
        tradeDetails.setTradeSubCategory(tradeSubCategory);
        return this;
    }

    public TradeDetailsBuilder withtradeAreaWeightOfPremises(String tradeAreaWeightOfPremises) {
        tradeDetails.setTradeAreaWeightOfPremises(tradeAreaWeightOfPremises);
        return this;
    }

    public TradeDetailsBuilder withremarks(String remarks) {
        tradeDetails.setRemarks(remarks);
        return this;
    }

    public TradeDetailsBuilder withtradeCommencementDate(String tradeCommencementDate) {
        tradeDetails.setTradeCommencementDate(tradeCommencementDate);
        return this;
    }

    public TradeDetails build() {
        return tradeDetails;
    }
}

