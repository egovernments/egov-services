package builders.tradeLicense;

import entities.tradeLicense.TradeLocationDetails;

public class TradeLocationDetailsBuilder {

    TradeLocationDetails tradeLocationDetails = new TradeLocationDetails();

    public TradeLocationDetailsBuilder withpropertyAssessmentNumber(String aadhaarNumber) {
        tradeLocationDetails.setPropertyAssessmentNumber(aadhaarNumber);
        return this;
    }

    public TradeLocationDetailsBuilder withownershipType(String ownershipType) {
        tradeLocationDetails.setOwnershipType((ownershipType));
        return this;
    }

    public TradeLocationDetailsBuilder withLocality(String locality) {
        tradeLocationDetails.setLocality(locality);
        return this;
    }

    public TradeLocationDetailsBuilder withWard(String ward) {
        tradeLocationDetails.setWard(ward);
        return this;
    }

    public TradeLocationDetails build() {
        return tradeLocationDetails;
    }
}
