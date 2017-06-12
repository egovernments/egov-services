package builders.collections;

import entities.collections.ChallanHeaderDetails;

public class ChallanHeaderDetailsBuilder {

    ChallanHeaderDetails challanHeaderDetails = new ChallanHeaderDetails();

    public ChallanHeaderDetailsBuilder withDate(String date) {
        challanHeaderDetails.setDate(date);
        return this;
    }

    public ChallanHeaderDetailsBuilder withPayeeName(String payeeName) {
        challanHeaderDetails.setPayeeName(payeeName);
        return this;
    }

    public ChallanHeaderDetailsBuilder withPayeeAddress(String payeeAddress) {
        challanHeaderDetails.setPayeeAddress(payeeAddress);
        return this;
    }

    public ChallanHeaderDetailsBuilder withNarration(String narration) {
        challanHeaderDetails.setNarration(narration);
        return this;
    }

    public ChallanHeaderDetailsBuilder withServiceCategory(String serviceCategory) {
        challanHeaderDetails.setServiceCategory(serviceCategory);
        return this;
    }

    public ChallanHeaderDetailsBuilder withServiceType(String serviceType) {
        challanHeaderDetails.setServiceType(serviceType);
        return this;
    }

    public ChallanHeaderDetailsBuilder withAmount(String amount) {
        challanHeaderDetails.setAmount(amount);
        return this;
    }

    public ChallanHeaderDetails build() {
        return challanHeaderDetails;
    }
}