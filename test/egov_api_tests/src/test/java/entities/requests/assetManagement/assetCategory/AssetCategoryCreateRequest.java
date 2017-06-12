package entities.requests.assetManagement.assetCategory;

import entities.requests.assetManagement.RequestInfo;
import org.codehaus.jackson.annotate.JsonProperty;

public class AssetCategoryCreateRequest {

    @JsonProperty("AssetCategory")
    private AssetCategory AssetCategory;

    @JsonProperty("RequestInfo")
    private RequestInfo RequestInfo;

    public AssetCategory getAssetCategory() {
        return AssetCategory;
    }

    public void setAssetCategory(AssetCategory AssetCategory) {
        this.AssetCategory = AssetCategory;
    }

    public RequestInfo getRequestInfo() {
        return RequestInfo;
    }

    public void setRequestInfo(RequestInfo RequestInfo) {
        this.RequestInfo = RequestInfo;
    }
}