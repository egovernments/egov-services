package builders.assetManagement.assetService;

import entities.assetManagement.assetService.HeaderDetails;

public final class HeaderDetailsBuilder {

    HeaderDetails headerDetails = new HeaderDetails();

    public HeaderDetailsBuilder withDepartment(String department) {
        headerDetails.setDepartment(department);
        return this;
    }

    public HeaderDetailsBuilder withAssetCategory(String assetCategory) {
        headerDetails.setAssetCategory(assetCategory);
        return this;
    }

    public HeaderDetailsBuilder withDateOfCreation(String dateOfCreation) {
        headerDetails.setDateOfCreation(dateOfCreation);
        return this;
    }

    public HeaderDetailsBuilder withDescription(String description) {
        headerDetails.setDescription(description);
        return this;
    }

    public HeaderDetailsBuilder withAssetName(String assetName) {
        headerDetails.setAssetName(assetName);
        return this;
    }

    public HeaderDetailsBuilder withModeOfAcquisition(String modeOfAcquisition) {
        headerDetails.setModeOfAcquisition(modeOfAcquisition);
        return this;
    }

    public HeaderDetails build() {
        return headerDetails;
    }
}
