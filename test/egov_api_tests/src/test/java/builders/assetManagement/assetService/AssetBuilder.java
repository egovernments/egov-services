package builders.assetManagement.assetService;

import entities.requests.assetManagement.assetService.Asset;
import entities.responses.assetManagement.assetService.AssetCategory;
import entities.responses.assetManagement.assetService.Department;
import entities.responses.assetManagement.assetService.LocationDetails;
import entities.responses.assetManagement.assetService.Properties;

public final class AssetBuilder {

    Asset asset = new Asset();
    Department department = new DepartmentBuilder().build();
    Properties properties = new PropertiesBuilder().build();
    LocationDetails locationDetails = new LocationDetailsBuilder().build();
    AssetCategory assetCategory = new AssetCategoryBuilder().build();

    public AssetBuilder() {
        asset.setTenantId("1");
        asset.setAssetDetails("Asset details");
        asset.setStatus("SOLD");
        asset.setDepartment(department);
        asset.setWidth("width");
        asset.setModeOfAcquisition("ACQUIRED");
        asset.setProperties(properties);
        asset.setRemarks("remarks");
        asset.setLocationDetails(locationDetails);
        asset.setAccumulatedDepreciation("20.67");
        asset.setDateOfCreation("08/12/2017");
        asset.setGrossValue("10.50");
        asset.setDescription("asset description");
        asset.setName("shopping mall");
        asset.setLength("length");
        asset.setTotalArea("total area");
        asset.setAssetCategory(assetCategory);
    }

    public AssetBuilder withTenantId(String tenantId) {
        asset.setTenantId(tenantId);
        return this;
    }

    public AssetBuilder withAssetDetails(String assetDetails) {
        asset.setAssetDetails(assetDetails);
        return this;
    }

    public AssetBuilder withStatus(String status) {
        asset.setStatus(status);
        return this;
    }

    public AssetBuilder withDepartment(Department department) {
        asset.setDepartment(department);
        return this;
    }

    public AssetBuilder withWidth(String width) {
        asset.setWidth(width);
        return this;
    }

    public AssetBuilder withModeOfAcquisition(String modeOfAcquisition) {
        asset.setModeOfAcquisition(modeOfAcquisition);
        return this;
    }

    public AssetBuilder withRemarks(String remarks) {
        asset.setRemarks(remarks);
        return this;
    }

    public AssetBuilder withProperties(Properties properties) {
        asset.setProperties(properties);
        return this;
    }

    public AssetBuilder withAccumulatedDepreciation(String accumulatedDepreciation) {
        asset.setAccumulatedDepreciation(accumulatedDepreciation);
        return this;
    }

    public AssetBuilder withDateOfCreation(String dateOfCreation) {
        asset.setDateOfCreation(dateOfCreation);
        return this;
    }

    public AssetBuilder withLocationDetails(LocationDetails locationDetails) {
        asset.setLocationDetails(locationDetails);
        return this;
    }

    public AssetBuilder withGrossValue(String grossValue) {
        asset.setGrossValue(grossValue);
        return this;
    }

    public AssetBuilder withDescription(String description) {
        asset.setDescription(description);
        return this;
    }

    public AssetBuilder withName(String name) {
        asset.setName(name);
        return this;
    }

    public AssetBuilder withLength(String length) {
        asset.setLength(length);
        return this;
    }

    public AssetBuilder withTotalArea(String totalArea) {
        asset.setTotalArea(totalArea);
        return this;
    }

    public AssetBuilder withAssetCategory(AssetCategory assetCategory) {
        asset.setAssetCategory(assetCategory);
        return this;
    }

    public Asset build() {
        return asset;
    }
}
