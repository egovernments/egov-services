package builders.assetManagement.assetService;

import builders.assetManagement.RequestInfoBuilder;
import entities.requests.assetManagement.RequestInfo;
import entities.requests.assetManagement.assetService.Asset;
import entities.requests.assetManagement.assetService.CreateAssetServiceRequest;

public final class CreateAssetServiceRequestBuilder {

    CreateAssetServiceRequest createAssetServiceRequest = new CreateAssetServiceRequest();
    Asset asset = new AssetBuilder().build();
    RequestInfo requestInfo = new RequestInfoBuilder()
            .withRequesterId("Ghanshyam").build();

    public CreateAssetServiceRequestBuilder() {
        createAssetServiceRequest.setAsset(asset);
        createAssetServiceRequest.setRequestInfo(requestInfo);
    }


    public CreateAssetServiceRequestBuilder withAsset(Asset Asset) {
        createAssetServiceRequest.setAsset(Asset);
        return this;
    }

    public CreateAssetServiceRequestBuilder withRequestInfo(RequestInfo RequestInfo) {
        createAssetServiceRequest.setRequestInfo(RequestInfo);
        return this;
    }

    public CreateAssetServiceRequest build() {
        return createAssetServiceRequest;
    }
}
