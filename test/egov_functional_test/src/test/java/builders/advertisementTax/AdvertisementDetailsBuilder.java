package builders.advertisementTax;

import entities.advertisementTax.AdvertisementDetails;

public class AdvertisementDetailsBuilder {

    AdvertisementDetails advertisementDetails = new AdvertisementDetails();

    public AdvertisementDetailsBuilder() {
    }

    public AdvertisementDetailsBuilder withCategory(String category) {
        advertisementDetails.setCategory(category);
        return this;
    }

    public AdvertisementDetailsBuilder withSubCategory(String subCategory) {
        advertisementDetails.setSubCategory(subCategory);
        return this;
    }

    public AdvertisementDetailsBuilder withClassType(String classType) {
        advertisementDetails.setClassType(classType);
        return this;
    }

    public AdvertisementDetailsBuilder withRevenueInspector(String revenueInspector) {
        advertisementDetails.setRevenueInspector(revenueInspector);
        return this;
    }

    public AdvertisementDetailsBuilder withPropertyType(String propertyType) {
        advertisementDetails.setPropertyType(propertyType);
        return this;
    }

    public AdvertisementDetails build() {
        return advertisementDetails;
    }
}
