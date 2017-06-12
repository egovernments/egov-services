package builders.assetManagement.assetService;

import entities.responses.assetManagement.assetService.AssetCategory;

public final class AssetCategoryBuilder {

    AssetCategory assetCategory = new AssetCategory();

    public AssetCategoryBuilder() {
        assetCategory.setTenantId("1");
        assetCategory.setId(31);
        assetCategory.setName("Land");
        assetCategory.setCode("00001");
        assetCategory.setAssetCategoryType("IMMOVABLE");
        assetCategory.setParent(null);
        assetCategory.setDepreciationMethod("STRAIGHT_LINE_METHOD");
        assetCategory.setAssetAccount("6");
        assetCategory.setAccumulatedDepreciationAccount("7");
        assetCategory.setRevaluationReserveAccount("8");
        assetCategory.setDepreciationExpenseAccount("9");
        assetCategory.setUnitOfMeasurement("10");
        assetCategory.setDepreciationRate(null);
        assetCategory.setCustomFields(null);
    }

    public AssetCategoryBuilder withUnitOfMeasurement(String unitOfMeasurement) {
        assetCategory.setUnitOfMeasurement(unitOfMeasurement);
        return this;
    }

    public AssetCategoryBuilder withParent(String parent) {
        assetCategory.setParent(parent);
        return this;
    }

    public AssetCategoryBuilder withCode(String code) {
        assetCategory.setCode(code);
        return this;
    }

    public AssetCategoryBuilder withAssetAccount(String assetAccount) {
        assetCategory.setAssetAccount(assetAccount);
        return this;
    }

    public AssetCategoryBuilder withDepreciationMethod(String depreciationMethod) {
        assetCategory.setDepreciationMethod(depreciationMethod);
        return this;
    }

    public AssetCategoryBuilder withCustomFields(String customFields) {
        assetCategory.setCustomFields(customFields);
        return this;
    }

    public AssetCategoryBuilder withAssetCategoryType(String assetCategoryType) {
        assetCategory.setAssetCategoryType(assetCategoryType);
        return this;
    }

    public AssetCategoryBuilder withDepreciationExpenseAccount(String depreciationExpenseAccount) {
        assetCategory.setDepreciationExpenseAccount(depreciationExpenseAccount);
        return this;
    }

    public AssetCategoryBuilder withRevaluationReserveAccount(String revaluationReserveAccount) {
        assetCategory.setRevaluationReserveAccount(revaluationReserveAccount);
        return this;
    }

    public AssetCategoryBuilder withTenantId(String tenantId) {
        assetCategory.setTenantId(tenantId);
        return this;
    }

    public AssetCategoryBuilder withName(String name) {
        assetCategory.setName(name);
        return this;
    }

    public AssetCategoryBuilder withId(int id) {
        assetCategory.setId(id);
        return this;
    }

    public AssetCategoryBuilder withDepreciationRate(String depreciationRate) {
        assetCategory.setDepreciationRate(depreciationRate);
        return this;
    }

    public AssetCategoryBuilder withAccumulatedDepreciationAccount(String accumulatedDepreciationAccount) {
        assetCategory.setAccumulatedDepreciationAccount(accumulatedDepreciationAccount);
        return this;
    }

    public AssetCategory build() {
        return assetCategory;
    }
}
