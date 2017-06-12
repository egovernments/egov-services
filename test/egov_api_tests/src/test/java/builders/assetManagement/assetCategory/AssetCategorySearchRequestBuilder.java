package builders.assetManagement.assetCategory;

import builders.assetManagement.RequestInfoBuilder;
import entities.requests.assetManagement.RequestInfo;
import entities.requests.assetManagement.SearchAssetRequest;

public final class AssetCategorySearchRequestBuilder {
    SearchAssetRequest searchAssetRequest = new SearchAssetRequest();

    RequestInfo requestInfo = new RequestInfoBuilder().build();

    public AssetCategorySearchRequestBuilder() {
        searchAssetRequest.setRequestInfo(requestInfo);
    }

    public AssetCategorySearchRequestBuilder withRequestInfo(RequestInfo requestInfo) {
        searchAssetRequest.setRequestInfo(requestInfo);
        return this;
    }

    public SearchAssetRequest build() {
        return searchAssetRequest;
    }
}
