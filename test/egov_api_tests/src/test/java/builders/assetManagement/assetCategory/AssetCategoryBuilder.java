package builders.assetManagement.assetCategory;

import entities.requests.assetManagement.assetCategory.AssetCategory;
import entities.requests.assetManagement.assetCategory.CustomFields;

public class AssetCategoryBuilder {

    AssetCategory assetCategory = new AssetCategory();

    CustomFields customFields1 = new CustomFieldsBuilder()
            .withName("Land")
            .withType("String")
            .withIsActive("true")
            .withIsMandatory("true")
            .withValues("abc")
            .withLocalText("localtext")
            .withregExFormate("regex")
            .build();

    CustomFields customFields2 = new CustomFieldsBuilder()
            .withName("PROPERTY")
            .withType("String")
            .withIsActive("true")
            .withIsMandatory("true")
            .withValues("abc")
            .withLocalText("localtext")
            .withregExFormate("regex")
            .build();

    CustomFields[] customFields = new CustomFields[2];

    public AssetCategoryBuilder() {
        assetCategory.setTenantId("1");
        assetCategory.setName("Street lighting");
        assetCategory.setAssetCategoryType("MOVABLE");
        assetCategory.setParent("32");
        assetCategory.setDepreciationMethod("STRAIGHT_LINE_METHOD");
        assetCategory.setAssetAccount("6");
        assetCategory.setAccumulatedDepreciationAccount("7");
        assetCategory.setRevaluationReserveAccount("8");
        assetCategory.setDepreciationExpenseAccount("9");
        assetCategory.setUnitOfMeasurement("10");
        customFields[0] = customFields1;
        customFields[1] = customFields2;
        assetCategory.setCustomFields(customFields);
    }

    public AssetCategory build() {
        return assetCategory;
    }
}
