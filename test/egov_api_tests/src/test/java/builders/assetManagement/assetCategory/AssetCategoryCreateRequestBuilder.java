package builders.assetManagement.assetCategory;

import builders.assetManagement.RequestInfoBuilder;
import entities.requests.assetManagement.RequestInfo;
import entities.requests.assetManagement.assetCategory.AssetCategory;
import entities.requests.assetManagement.assetCategory.AssetCategoryCreateRequest;

public class AssetCategoryCreateRequestBuilder {

    AssetCategoryCreateRequest request = new AssetCategoryCreateRequest();
    AssetCategory AssetCategory = new AssetCategoryBuilder().build();
    RequestInfo RequestInfo = new RequestInfoBuilder().build();

    public AssetCategoryCreateRequestBuilder() {
        request.setRequestInfo(RequestInfo);
        request.setAssetCategory(AssetCategory);
    }

    public AssetCategoryCreateRequestBuilder withAssetCategory(AssetCategory assetCategory) {
        request.setAssetCategory(assetCategory);
        return this;
    }

    public AssetCategoryCreateRequestBuilder withRequestInfo(RequestInfo requestInfo) {
        request.setRequestInfo(requestInfo);
        return this;
    }

    public AssetCategoryCreateRequest build() {
        return request;
    }
}
