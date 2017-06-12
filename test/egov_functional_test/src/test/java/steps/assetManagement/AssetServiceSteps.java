package steps.assetManagement;

import cucumber.api.java8.En;
import entities.assetManagement.assetService.HeaderDetails;
import entities.assetManagement.assetService.LocationDetails;
import excelDataFiles.AssetServiceDataReader;
import pages.assetManagement.AssetServicePage;
import steps.BaseSteps;

public class AssetServiceSteps extends BaseSteps implements En {

    public AssetServiceSteps() {

        And("^user will enter the details as (\\w+) and (\\w+)$", (String headerDetails
                , String locationDetails) -> {

            HeaderDetails headerDetails1 = new AssetServiceDataReader(assetTestDataFileName)
                    .getHeaderDetails(headerDetails);
            scenarioContext.setAssetCategory(headerDetails1.getAssetCategory());
            pageStore.get(AssetServicePage.class).enterHeaderDetails(headerDetails1);

            LocationDetails locationDetails1 = new AssetServiceDataReader(assetTestDataFileName)
                    .getLocationDetails(locationDetails);
            pageStore.get(AssetServicePage.class).enterLocationDetails(locationDetails1);

        });

        And("^user will enter the category details as (\\w+) and with asset summary status as (\\w+)$", (
                String categoryDetails, String assetStatus) -> {

            pageStore.get(AssetServicePage.class).enterCategoryDetails(categoryDetails);
            pageStore.get(AssetServicePage.class).enterAssetStatusDetails(assetStatus);
        });

        And("^user will be notified the success page with an asset application number$", () -> {
            String assetServiceNumber = pageStore.get(AssetServicePage.class).getAssetServiceNumber();
            scenarioContext.setApplicationNumber(assetServiceNumber);
        });

        And("^user will search the asset application based on category details$", () -> {
            pageStore.get(AssetServicePage.class).searchApplicationBasedOnCategory(
                    scenarioContext.getAssetCategory(), scenarioContext.getApplicationNumber());
        });

        And("^user will update the details in asset modify screen based on (\\w+)$", (String categoryDetails) -> {
            String message = pageStore.get(AssetServicePage.class).enterAssetDetailsToUpdate(categoryDetails);
            scenarioContext.setActualMessage(message);

            pageStore.get(AssetServicePage.class).closeAssetViewPage();
        });
    }
}

