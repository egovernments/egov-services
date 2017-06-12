package steps.assetManagement;


import cucumber.api.java8.En;
import entities.assetManagement.AssetCategoryDetails;
import entities.assetManagement.CustomFieldsDetails;
import excelDataFiles.AssetCategoryDataReader;
import pages.assetManagement.AssetCategoryPage;
import steps.BaseSteps;

public class AssetCategorySteps extends BaseSteps implements En {

    public AssetCategorySteps() {

        And("^user will enter the details of asset category as (\\w+)$", (String assetCategory) -> {
            AssetCategoryDetails details = new AssetCategoryDataReader(assetTestDataFileName)
                    .getAssetCategoryDetails(assetCategory);
            scenarioContext.setApplicationNumber(details.getName());
            pageStore.get(AssetCategoryPage.class).enterAssetCategoryDetails(details);
        });
        And("^user will enter the details of custom fields as (\\w+)$", (String noOfCustomFields) -> {
            for (int i = 0; i < Integer.parseInt(noOfCustomFields); i++) {
                pageStore.get(AssetCategoryPage.class).clickToCreateCustomFields();
                CustomFieldsDetails details = new AssetCategoryDataReader(assetTestDataFileName)
                        .getCustomFieldsDetails(scenarioContext.getApplicationNumber() + (i + 1));
                pageStore.get(AssetCategoryPage.class).enterCustomFieldsDetails(details);
                pageStore.get(AssetCategoryPage.class).addOrEditCustomFieldsButton();
            }
        });
        And("^user create the asset category$", () -> {
            pageStore.get(AssetCategoryPage.class).clickOnCreateAssetCategoryButton();
        });
    }
}
