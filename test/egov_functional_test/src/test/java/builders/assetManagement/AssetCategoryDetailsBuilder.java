package builders.assetManagement;

import entities.assetManagement.AssetCategoryDetails;


public final class AssetCategoryDetailsBuilder {

    AssetCategoryDetails AssetCategoryDetails = new AssetCategoryDetails();

    public AssetCategoryDetailsBuilder() {
    }

    public AssetCategoryDetailsBuilder withName(String name) {
        AssetCategoryDetails.setName(name);
        return this;
    }

    public AssetCategoryDetailsBuilder withAssetCategoryType(String assetCategoryType) {
        AssetCategoryDetails.setAssetCategoryType(assetCategoryType);
        return this;
    }

    public AssetCategoryDetailsBuilder withParentCategory(String parentCategory) {
        AssetCategoryDetails.setParentCategory(parentCategory);
        return this;
    }

    public AssetCategoryDetailsBuilder withDepreciationMethod(String depreciationMethod) {
        AssetCategoryDetails.setDepreciationMethod(depreciationMethod);
        return this;
    }

    public AssetCategoryDetailsBuilder withAssetAccountCode(String assetAccountCode) {
        AssetCategoryDetails.setAssetAccountCode(assetAccountCode);
        return this;
    }

    public AssetCategoryDetailsBuilder withAccumulatedDepreciationCode(String accumulatedDepreciationCode) {
        AssetCategoryDetails.setAccumulatedDepreciationCode(accumulatedDepreciationCode);
        return this;
    }

    public AssetCategoryDetailsBuilder withRevaluationReserveAccountCode(String revaluationReserveAccountCode) {
        AssetCategoryDetails.setRevaluationReserveAccountCode(revaluationReserveAccountCode);
        return this;
    }

    public AssetCategoryDetailsBuilder withDepreciationExpenceAccount(String depreciationExpenceAccount) {
        AssetCategoryDetails.setDepreciationExpenceAccount(depreciationExpenceAccount);
        return this;
    }

    public AssetCategoryDetailsBuilder withUOM(String UOM) {
        AssetCategoryDetails.setUOM(UOM);
        return this;
    }

    public AssetCategoryDetails build() {
        return AssetCategoryDetails;
    }
}
