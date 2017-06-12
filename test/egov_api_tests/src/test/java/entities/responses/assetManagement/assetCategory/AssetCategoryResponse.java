package entities.responses.assetManagement.assetCategory;

import entities.requests.assetManagement.assetCategory.AssetCategory;
import org.codehaus.jackson.annotate.JsonProperty;

public class AssetCategoryResponse {
    private String ResponseInfo;

    @JsonProperty("AssetCategory")
    private AssetCategory[] AssetCategory;

    public String getResponseInfo() {
        return ResponseInfo;
    }

    public void setResponseInfo(String ResponseInfo) {
        this.ResponseInfo = ResponseInfo;
    }

    public AssetCategory[] getAssetCategory() {
        return AssetCategory;
    }

    public void setAssetCategory(AssetCategory[] AssetCategory) {
        this.AssetCategory = AssetCategory;
    }

}
